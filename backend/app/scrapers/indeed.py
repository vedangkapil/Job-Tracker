import asyncio
from datetime import datetime
from typing import Dict, List
from urllib.parse import urlencode

import requests
from bs4 import BeautifulSoup

from backend.app.config import settings


class IndeedScraper:
    """Fetch the public Indeed search page for the requested query."""

    def __init__(self):
        self.base_url = settings.indeed_base_url.rstrip("/")
        self.max_jobs = settings.max_jobs_per_search

    async def scrape_jobs(self, keyword: str, location: str) -> List[Dict]:
        return await asyncio.to_thread(self._scrape_jobs, keyword, location)

    def _scrape_jobs(self, keyword: str, location: str) -> List[Dict]:
        query = urlencode({"q": keyword.strip(), "l": location.strip()})
        response = requests.get(
            f"{self.base_url}/jobs?{query}",
            headers={"User-Agent": "Mozilla/5.0 (compatible; JobTracker/1.0)"},
            timeout=15,
        )
        response.raise_for_status()
        soup = BeautifulSoup(response.text, "html.parser")
        jobs: List[Dict] = []

        for card in soup.select("div.job_seen_beacon, div.cardOutline"):
            title_node = card.select_one("h2.jobTitle span, h2.jobTitle a, a.jcs-JobTitle")
            company_node = card.select_one("[data-testid='company-name'], span.companyName")
            location_node = card.select_one("[data-testid='text-location'], div.companyLocation")
            link_node = card.select_one("a.jcs-JobTitle, h2.jobTitle a")
            if not (title_node and company_node and link_node):
                continue

            href = link_node.get("href", "")
            if href.startswith("/"):
                href = f"{self.base_url}{href}"
            if not href:
                continue
            description_node = card.select_one(".job-snippet, [data-testid='job-snippet']")
            jobs.append({
                "title": title_node.get_text(" ", strip=True),
                "company": company_node.get_text(" ", strip=True),
                "location": location_node.get_text(" ", strip=True) if location_node else location,
                "description": description_node.get_text(" ", strip=True) if description_node else "",
                "salary": None,
                "job_url": href,
                "source": "indeed",
                "scraped_at": datetime.utcnow(),
                "keywords": f"{keyword},{location}",
            })
            if len(jobs) >= self.max_jobs:
                break
        return jobs


indeed_scraper = IndeedScraper()
