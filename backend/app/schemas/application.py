from pydantic import BaseModel
from datetime import datetime
from typing import Optional

class ApplicationBase(BaseModel):
    job_id: int
    status: str = "applied"
    notes: Optional[str] = None
    interview_date: Optional[datetime] = None
    follow_up_date: Optional[datetime] = None
    contact_person: Optional[str] = None
    contact_email: Optional[str] = None
    salary_offered: Optional[str] = None

class ApplicationCreate(ApplicationBase):
    pass

class ApplicationUpdate(BaseModel):
    status: Optional[str] = None
    notes: Optional[str] = None
    interview_date: Optional[datetime] = None
    follow_up_date: Optional[datetime] = None
    contact_person: Optional[str] = None
    contact_email: Optional[str] = None
    salary_offered: Optional[str] = None

class Application(ApplicationBase):
    id: int
    applied_date: datetime
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True