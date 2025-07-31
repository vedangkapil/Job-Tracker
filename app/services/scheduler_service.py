from apscheduler.schedulers.asyncio import AsyncIOScheduler
from apscheduler.triggers.interval import IntervalTrigger
from typing import List, Dict, Optional
from sqlmodel import Session
from app.database import get_session
from app.scrapers.manager import scraper_manager
from app.config import settings
import logging

logger = logging.getLogger(__name__)

class JobScheduler:
    def __init__(self):
        self.scheduler = AsyncIOScheduler()
        self.job_configs = []
        self.is_running = False
    
    def add_job_config(self, keyword: str, location: str, sources: List[str] = None, interval_hours: int = None):
        """Add a job configuration for periodic scraping."""
        if interval_hours is None:
            interval_hours = settings.scrape_interval_hours
        
        config = {
            "keyword": keyword,
            "location": location,
            "sources": sources or ["indeed", "linkedin"],
            "interval_hours": interval_hours
        }
        
        self.job_configs.append(config)
        logger.info(f"Added job config: {keyword} in {location} every {interval_hours} hours")
    
    def remove_job_config(self, keyword: str, location: str):
        """Remove a job configuration."""
        self.job_configs = [config for config in self.job_configs 
                           if not (config["keyword"] == keyword and config["location"] == location)]
        logger.info(f"Removed job config: {keyword} in {location}")
    
    async def scrape_job(self, keyword: str, location: str, sources: List[str]):
        """Scrape jobs for a specific configuration."""
        try:
            logger.info(f"Starting scheduled scrape for {keyword} in {location}")
            jobs = await scraper_manager.scrape_and_save_jobs(keyword, location, sources)
            logger.info(f"Completed scheduled scrape for {keyword} in {location}: {len(jobs)} jobs found")
        except Exception as e:
            logger.error(f"Error in scheduled scrape for {keyword} in {location}: {e}")
    
    def start_scheduler(self):
        """Start the scheduler with all configured jobs."""
        if self.is_running:
            logger.warning("Scheduler is already running")
            return
        
        if not settings.scheduler_enabled:
            logger.info("Scheduler is disabled in configuration")
            return
        
        for config in self.job_configs:
            job_id = f"scrape_{config['keyword']}_{config['location']}"
            
            # Add the job to the scheduler
            self.scheduler.add_job(
                func=self.scrape_job,
                trigger=IntervalTrigger(hours=config["interval_hours"]),
                args=[config["keyword"], config["location"], config["sources"]],
                id=job_id,
                name=f"Scrape {config['keyword']} in {config['location']}",
                replace_existing=True
            )
        
        self.scheduler.start()
        self.is_running = True
        logger.info(f"Scheduler started with {len(self.job_configs)} jobs")
    
    def stop_scheduler(self):
        """Stop the scheduler."""
        if self.is_running:
            self.scheduler.shutdown()
            self.is_running = False
            logger.info("Scheduler stopped")
    
    def get_scheduled_jobs(self) -> List[Dict]:
        """Get information about all scheduled jobs."""
        jobs = []
        for job in self.scheduler.get_jobs():
            jobs.append({
                "id": job.id,
                "name": job.name,
                "next_run_time": job.next_run_time.isoformat() if job.next_run_time else None,
                "trigger": str(job.trigger)
            })
        return jobs
    
    def pause_job(self, job_id: str):
        """Pause a specific job."""
        self.scheduler.pause_job(job_id)
        logger.info(f"Paused job: {job_id}")
    
    def resume_job(self, job_id: str):
        """Resume a specific job."""
        self.scheduler.resume_job(job_id)
        logger.info(f"Resumed job: {job_id}")
    
    def remove_job(self, job_id: str):
        """Remove a specific job from the scheduler."""
        self.scheduler.remove_job(job_id)
        logger.info(f"Removed job: {job_id}")

# Create a global scheduler instance
job_scheduler = JobScheduler() 