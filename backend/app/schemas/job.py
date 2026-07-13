from pydantic import BaseModel
from datetime import datetime
from typing import Optional

class JobBase(BaseModel):
    title: str
    company: str
    location: str
    description: str
    salary: Optional[str] = None
    job_url: str
    source: str = "indeed"
    keywords: Optional[str] = None
    is_tracked: bool = False

class JobCreate(JobBase):
    pass

class JobUpdate(BaseModel):
    title: Optional[str] = None
    company: Optional[str] = None
    location: Optional[str] = None
    description: Optional[str] = None
    salary: Optional[str] = None
    job_url: Optional[str] = None
    source: Optional[str] = None
    keywords: Optional[str] = None
    is_tracked: Optional[bool] = None

class Job(JobBase):
    id: int
    scraped_at: datetime
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True

class JobSearch(BaseModel):
    keyword: str
    location: str
    sources: Optional[list] = ["indeed", "linkedin"]
    max_results: Optional[int] = 50