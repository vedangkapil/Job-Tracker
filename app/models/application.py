from sqlmodel import SQLModel, Field
from datetime import datetime
from typing import Optional

class Application(SQLModel, table=True):
    id: Optional[int] = Field(default=None, primary_key=True)
    job_id: int = Field(foreign_key="job.id")
    status: str = Field(default="applied")  # applied, interviewed, offered, rejected, etc.
    notes: Optional[str] = None
    applied_date: datetime = Field(default_factory=datetime.utcnow)
    interview_date: Optional[datetime] = None
    follow_up_date: Optional[datetime] = None
    contact_person: Optional[str] = None
    contact_email: Optional[str] = None
    salary_offered: Optional[str] = None
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)