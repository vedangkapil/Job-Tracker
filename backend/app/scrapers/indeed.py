import asyncio
from typing import List, Dict, Optional
from datetime import datetime
from backend.app.config import settings

class IndeedScraper:
    def __init__(self):
        self.base_url = settings.indeed_base_url
        self.max_jobs = settings.max_jobs_per_search
        self.delay = settings.scrape_delay_seconds
        
    async def scrape_jobs(self, keyword: str, location: str) -> List[Dict]:
        """Mock scraper that returns sample job data for demonstration."""
        # Simulate some delay
        await asyncio.sleep(1)
        
        # Return mock job data
        mock_jobs = [
            {
                "title": f"Senior {keyword.title()}",
                "company": "Tech Solutions Inc.",
                "location": location,
                "description": f"Exciting opportunity for a senior {keyword} to join our growing team. We're looking for someone with 3+ years of experience.",
                "salary": "$80,000 - $120,000",
                "job_url": f"https://indeed.com/viewjob?jk=123456_{keyword}",
                "source": "indeed",
                "scraped_at": datetime.utcnow(),
                "keywords": f"{keyword},{location}"
            },
            {
                "title": f"Junior {keyword.title()} Developer",
                "company": "StartUp Co",
                "location": location,
                "description": f"Great opportunity for a junior {keyword} developer to grow their skills in a fast-paced environment.",
                "salary": "$60,000 - $80,000",
                "job_url": f"https://indeed.com/viewjob?jk=789012_{keyword}",
                "source": "indeed",
                "scraped_at": datetime.utcnow(),
                "keywords": f"{keyword},{location}"
            },
            {
                "title": f"{keyword.title()} Engineer",
                "company": "Big Tech Corp",
                "location": location,
                "description": f"We're hiring a {keyword} engineer to work on cutting-edge projects. Remote work available.",
                "salary": "$90,000 - $130,000",
                "job_url": f"https://indeed.com/viewjob?jk=345678_{keyword}",
                "source": "indeed",
                "scraped_at": datetime.utcnow(),
                "keywords": f"{keyword},{location}"
            },
            {
                "title": f"Lead {keyword.title()}",
                "company": "Innovation Labs",
                "location": location,
                "description": f"Lead {keyword} position with team management responsibilities. Great benefits and growth opportunities.",
                "salary": "$100,000 - $150,000",
                "job_url": f"https://indeed.com/viewjob?jk=901234_{keyword}",
                "source": "indeed",
                "scraped_at": datetime.utcnow(),
                "keywords": f"{keyword},{location}"
            },
            {
                "title": f"Full Stack {keyword.title()}",
                "company": "Digital Agency",
                "location": location,
                "description": f"Full stack {keyword} developer needed for client projects. Experience with modern frameworks required.",
                "salary": "$70,000 - $100,000",
                "job_url": f"https://indeed.com/viewjob?jk=567890_{keyword}",
                "source": "indeed",
                "scraped_at": datetime.utcnow(),
                "keywords": f"{keyword},{location}"
            }
        ]
        
        print(f"Mock scraping: Found {len(mock_jobs)} jobs for '{keyword}' in '{location}'")
        return mock_jobs

# Create a global instance
indeed_scraper = IndeedScraper()