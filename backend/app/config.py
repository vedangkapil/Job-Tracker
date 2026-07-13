from pydantic import BaseSettings
from typing import Optional

class Settings(BaseSettings):
    database_url: str = "sqlite:///./job_tracker.db"
    secret_key: str = "your-secret-key-here"
    indeed_base_url: str = "https://www.indeed.com"
    linkedin_base_url: str = "https://www.linkedin.com/jobs"
    
    # Scheduler settings
    scheduler_enabled: bool = True
    scrape_interval_hours: int = 6
    
    # Scraping settings
    max_jobs_per_search: int = 50
    scrape_delay_seconds: int = 2
    
    class Config:
        env_file = ".env"

settings = Settings()