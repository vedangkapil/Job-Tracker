from typing import List, Optional
from sqlmodel import Session, select
from backend.app.models.application import Application
from backend.app.models.job import Job
from backend.app.schemas.application import ApplicationCreate, ApplicationUpdate
from datetime import datetime

class ApplicationService:
    def __init__(self, session: Session):
        self.session = session
    
    def get_applications(self, skip: int = 0, limit: int = 100) -> List[Application]:
        """Get all applications with pagination."""
        return self.session.exec(select(Application).offset(skip).limit(limit)).all()
    
    def get_application(self, application_id: int) -> Optional[Application]:
        """Get a specific application by ID."""
        return self.session.get(Application, application_id)
    
    def create_application(self, application: ApplicationCreate) -> Application:
        """Create a new application."""
        # Verify that the job exists
        job = self.session.get(Job, application.job_id)
        if not job:
            raise ValueError("Job not found")
        
        db_application = Application(**application.dict())
        self.session.add(db_application)
        self.session.commit()
        self.session.refresh(db_application)
        return db_application
    
    def update_application(self, application_id: int, application: ApplicationUpdate) -> Optional[Application]:
        """Update an application."""
        db_application = self.session.get(Application, application_id)
        if db_application:
            application_data = application.dict(exclude_unset=True)
            for key, value in application_data.items():
                setattr(db_application, key, value)
            db_application.updated_at = datetime.utcnow()
            self.session.add(db_application)
            self.session.commit()
            self.session.refresh(db_application)
        return db_application
    
    def delete_application(self, application_id: int) -> bool:
        """Delete an application."""
        db_application = self.session.get(Application, application_id)
        if db_application:
            self.session.delete(db_application)
            self.session.commit()
            return True
        return False
    
    def get_applications_by_status(self, status: str) -> List[Application]:
        """Get applications by status."""
        return self.session.exec(select(Application).where(Application.status == status)).all()
    
    def get_applications_by_job(self, job_id: int) -> List[Application]:
        """Get all applications for a specific job."""
        return self.session.exec(select(Application).where(Application.job_id == job_id)).all()
    
    def get_applications_with_jobs(self, skip: int = 0, limit: int = 100) -> List[dict]:
        """Get applications with their associated job information."""
        applications = self.session.exec(
            select(Application).offset(skip).limit(limit)
        ).all()
        
        result = []
        for app in applications:
            job = self.session.get(Job, app.job_id)
            app_data = app.dict()
            app_data["job"] = job.dict() if job else None
            result.append(app_data)
        
        return result