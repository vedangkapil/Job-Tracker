from typing import List, Dict, Optional
from app.scrapers.indeed import indeed_scraper
from app.scrapers.linkedin import linkedin_scraper
from app.models.job import Job
from app.database import get_session
from sqlmodel import Session, select
import asyncio

class ScraperManager:
    def __init__(self):
        self.scrapers = {
            "indeed": indeed_scraper,
            "linkedin": linkedin_scraper
        }
    
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
                    all_jobs.extend(jobs)
                    print(f"Found {len(jobs)} jobs from {source}")
                except Exception as e:
                    print(f"Error scraping from {source}: {e}")
                    continue
        
        return all_jobs
    
    async def scrape_and_save_jobs(self, keyword: str, location: str, sources: List[str] = None) -> List[Job]:
        """Scrape jobs and save them to the database."""
        scraped_jobs = await self.scrape_all_sources(keyword, location, sources)
        saved_jobs = []
        
        for session in get_session():
            try:
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
                
            except Exception as e:
                print(f"Error saving jobs to database: {e}")
                session.rollback()
            finally:
                session.close()
        
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