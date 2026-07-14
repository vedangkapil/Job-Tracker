import asyncio
from datetime import datetime
from typing import Dict, List

import requests
from bs4 import BeautifulSoup

from backend.app.config import settings


class LinkedInScraper:
    """Fetch public LinkedIn job cards for the requested query and location."""

    def __init__(self):
        self.base_url = settings.linkedin_base_url.rstrip("/")
        self.max_jobs = settings.max_jobs_per_search

    async def scrape_jobs(self, keyword: str, location: str) -> List[Dict]:
        return await asyncio.to_thread(self._scrape_jobs, keyword, location)

    def _scrape_jobs(self, keyword: str, location: str) -> List[Dict]:
        response = requests.get(
            f"{self.base_url}/search",
            params={"keywords": keyword.strip(), "location": location.strip()},
            headers={"User-Agent": "Mozilla/5.0 (compatible; JobTracker/1.0)"},
            timeout=15,
        )
        response.raise_for_status()
        soup = BeautifulSoup(response.text, "html.parser")
        jobs: List[Dict] = []

        # LinkedIn currently uses a div for each public job card.  Keep the
        # list-item selector too, since it is used by older page variants.
        for card in soup.select("div.base-card, li.base-card"):
            title_node = card.select_one("h3.base-search-card__title")
            company_node = card.select_one("h4.base-search-card__subtitle")
            location_node = card.select_one("span.job-search-card__location")
            link_node = card.select_one("a.base-card__full-link")
            if not (title_node and company_node and link_node):
                continue
            href = link_node.get("href", "").split("?")[0]
            if not href:
                continue
            jobs.append({
                "title": title_node.get_text(" ", strip=True),
                "company": company_node.get_text(" ", strip=True),
                "location": location_node.get_text(" ", strip=True) if location_node else location,
                "description": "Description available on the LinkedIn job posting.",
                "salary": None,
                "job_url": href,
                "source": "linkedin",
                "scraped_at": datetime.utcnow(),
                "keywords": f"{keyword},{location}",
            })
            if len(jobs) >= self.max_jobs:
                break
        return jobs


linkedin_scraper = LinkedInScraper()
