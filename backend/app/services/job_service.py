from typing import List, Optional
from sqlmodel import Session, select
from backend.app.models.job import Job
from backend.app.schemas.job import JobCreate, JobUpdate
from backend.app.scrapers.manager import scraper_manager
from datetime import datetime

class JobService:
    def __init__(self, session: Session):
        self.session = session
    
    def get_jobs(self, skip: int = 0, limit: int = 100) -> List[Job]:
        """Get all jobs with pagination."""
        return self.session.exec(select(Job).offset(skip).limit(limit)).all()
    
    def get_job(self, job_id: int) -> Optional[Job]:
        """Get a specific job by ID."""
        return self.session.get(Job, job_id)
    def create_job(self, job: JobCreate) -> Job:
        """Create a new job manually."""
        db_job = Job(**job.dict())
        self.session.add(db_job)
        self.session.commit()
        self.session.refresh(db_job)
        return db_job
    
    def update_job(self, job_id: int, job: JobUpdate) -> Optional[Job]:
        """Update a job."""
        db_job = self.session.get(Job, job_id)
        if db_job:
            job_data = job.dict(exclude_unset=True)
            for key, value in job_data.items():
                setattr(db_job, key, value)
            db_job.updated_at = datetime.utcnow()
            self.session.add(db_job)
            self.session.commit()
            self.session.refresh(db_job)
        return db_job
    
    def delete_job(self, job_id: int) -> bool:
        """Delete a job."""
        db_job = self.session.get(Job, job_id)
        if db_job:
            self.session.delete(db_job)
            self.session.commit()
            return True
        return False
    
    def search_jobs(self, keyword: str = None, location: str = None, source: str = None) -> List[Job]:
        """Search jobs by keyword, location, or source."""
        query = select(Job)
        
        if keyword:
            query = query.where(Job.title.contains(keyword) | Job.description.contains(keyword))
        if location:
            query = query.where(Job.location.contains(location))
        if source:
            query = query.where(Job.source == source)
        
        return self.session.exec(query).all()
    
    def get_tracked_jobs(self) -> List[Job]:
        """Get all tracked jobs."""
        return self.session.exec(select(Job).where(Job.is_tracked == True)).all()
    
    def get_untracked_jobs(self) -> List[Job]:
        """Get all untracked jobs."""
        return self.session.exec(select(Job).where(Job.is_tracked == False)).all()
    
    def mark_job_as_tracked(self, job_id: int) -> bool:
        """Mark a job as tracked."""
        db_job = self.session.get(Job, job_id)
        if db_job:
            db_job.is_tracked = True
            db_job.updated_at = datetime.utcnow()
            self.session.add(db_job)
            self.session.commit()
            return True
        return False
    
    def mark_job_as_untracked(self, job_id: int) -> bool:
        """Mark a job as untracked."""
        db_job = self.session.get(Job, job_id)
        if db_job:
            db_job.is_tracked = False
            db_job.updated_at = datetime.utcnow()
            self.session.add(db_job)
            self.session.commit()
            return True
        return False
    
    def toggle_job_tracking(self, job_id: int) -> bool:
        """Toggle job tracking status."""
        db_job = self.session.get(Job, job_id)
        if db_job:
            db_job.is_tracked = not db_job.is_tracked
            db_job.updated_at = datetime.utcnow()
            self.session.add(db_job)
            self.session.commit()
            return True
        return False
    
    async def scrape_and_save_jobs(self, keyword: str, location: str, sources: List[str] = None) -> List[Job]:
        """Scrape jobs and save them to the database."""
        return await scraper_manager.scrape_and_save_jobs(keyword, location, sources, self.session)
