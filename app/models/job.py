from sqlmodel import SQLModel, Field
from datetime import datetime
from typing import Optional

class Job(SQLModel, table=True):
    id: Optional[int] = Field(default=None, primary_key=True)
    title: str
    company: str
    location: str
    description: str
    salary: Optional[str] = None
    job_url: str
    source: str = Field(default="indeed")  # indeed, linkedin, etc.
    scraped_at: datetime = Field(default_factory=datetime.utcnow)
    keywords: Optional[str] = None
    is_tracked: bool = Field(default=False)
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)