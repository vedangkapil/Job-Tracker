from typing import List, Dict, Optional
from backend.app.scrapers.indeed import indeed_scraper
from backend.app.scrapers.linkedin import linkedin_scraper
from backend.app.models.job import Job
from sqlmodel import Session, select
import re

class ScraperManager:
    LEGACY_DEMO_COMPANIES = (
        "Tech Solutions Inc.",
        "StartUp Co",
        "Big Tech Corp",
        "Innovation Labs",
        "Digital Agency",
        "LinkedIn Tech",
        "Enterprise Solutions",
        "Consulting Partners",
    )

    def __init__(self):
        self.scrapers = {
            "indeed": indeed_scraper,
            "linkedin": linkedin_scraper
        }

    @staticmethod
    def _matches_request(job: Dict, keyword: str, location: str) -> bool:
        """Keep only source results that match both parts of the user's search."""
        keyword_terms = re.findall(r"[a-z0-9]+", keyword.lower())
        search_text = " ".join((job.get("title", ""), job.get("description", ""))).lower()
        job_location = job.get("location", "").lower()
        return (
            all(term in search_text for term in keyword_terms)
            and location.strip().lower() in job_location
        )

    @classmethod
    def _remove_legacy_demo_jobs(cls, session: Session) -> None:
        """Remove rows created by the former fixed mock scrapers only."""
        legacy_jobs = session.exec(
            select(Job).where(Job.company.in_(cls.LEGACY_DEMO_COMPANIES))
        ).all()
        for job in legacy_jobs:
            session.delete(job)
    
    async def scrape_all_sources(self, keyword: str, location: str, sources: List[str] = None) -> List[Dict]:
        """Scrape jobs from multiple sources."""
        if sources is None:
            sources = ["indeed", "linkedin"]
        
        all_jobs = []
        
        for source in sources:
            if source in self.scrapers:
                try:
                    print(f"Scraping from {source}...")
                    jobs = await self.scrapers[source].scrape_jobs(keyword, location)
                    jobs = [job for job in jobs if self._matches_request(job, keyword, location)]
                    all_jobs.extend(jobs)
                    print(f"Found {len(jobs)} jobs from {source}")
                except Exception as e:
                    print(f"Error scraping from {source}: {e}")
                    continue
        
        return all_jobs
    
    async def scrape_and_save_jobs(
        self, keyword: str, location: str, sources: List[str] = None, session: Optional[Session] = None
    ) -> List[Job]:
        """Scrape jobs and save them to the database."""
        scraped_jobs = await self.scrape_all_sources(keyword, location, sources)
        saved_jobs = []
        
        if session is None:
            raise ValueError("A database session is required to save scraped jobs")

        try:
            # The old application stored eight fixed demo jobs for every query.
            # Clear those invalid rows even if this real search returns no jobs.
            self._remove_legacy_demo_jobs(session)
            for job_data in scraped_jobs:
                # Check if job already exists
                existing_job = session.exec(
                    select(Job).where(Job.job_url == job_data["job_url"])
                ).first()

                if not existing_job:
                    # Create new job
                    job = Job(**job_data)
                    session.add(job)
                    saved_jobs.append(job)
                else:
                    # Update existing job
                    for key, value in job_data.items():
                        if key != "id":
                            setattr(existing_job, key, value)
                    existing_job.updated_at = job_data["scraped_at"]
                    saved_jobs.append(existing_job)
            session.commit()
            for job in saved_jobs:
                session.refresh(job)
        except Exception:
            session.rollback()
            raise
        
        return saved_jobs
    
    async def get_tracked_jobs(self, session: Session) -> List[Job]:
        """Get all jobs that are being tracked."""
        return session.exec(select(Job).where(Job.is_tracked == True)).all()
    
    async def mark_job_as_tracked(self, job_id: int, session: Session) -> bool:
        """Mark a job as tracked."""
        job = session.get(Job, job_id)
        if job:
            job.is_tracked = True
            session.add(job)
            session.commit()
            return True
        return False

# Create a global instance
scraper_manager = ScraperManager()
