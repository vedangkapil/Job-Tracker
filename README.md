# Job Tracker

A FastAPI-based job scraping and tracking project with a backend that collects job listings from multiple sources and stores them locally in a SQLite database.

The implementation is currently organized under the backend folder. The Android folder is present as a separate workspace for future mobile development.

## Features

- Scrape job listings from Indeed and LinkedIn
- Store scraped jobs in a local SQLite database
- Search jobs by keyword, location, or source
- Mark jobs as tracked or untracked
- Expose the backend through a FastAPI API

## Current Project Structure

```text
Job-Tracker/
├── README.md
├── android/                  # Android project folder (currently empty placeholder)
├── backend/
│   ├── QUICK_START.md
│   ├── main.py               # FastAPI app entry point
│   ├── requirements.txt
│   ├── setup.py
│   ├── test_app.py
│   └── app/
│       ├── __init__.py
│       ├── config.py
│       ├── database.py
│       ├── models/
│       │   ├── __init__.py
│       │   └── job.py
│       ├── schemas/
│       │   ├── __init__.py
│       │   └── job.py
│       ├── scrapers/
│       │   ├── __init__.py
│       │   ├── indeed.py
│       │   ├── linkedin.py
│       │   └── manager.py
│       ├── api/
│       │   ├── __init__.py
│       │   └── jobs.py
│       └── services/
│           ├── __init__.py
│           ├── job_service.py
│           └── scheduler_service.py
```

## Setup

### 1. Create and activate a virtual environment

macOS/Linux:

```bash
python3 -m venv venv
source venv/bin/activate
```

Windows:

```bash
python -m venv venv
venv\Scripts\activate
```

### 2. Install dependencies

```bash
pip install -r backend/requirements.txt
```

### 3. Run the backend

From the project root:

```bash
cd backend
uvicorn main:app --reload
```

The API will be available at:

- http://localhost:8000/docs
- http://localhost:8000/redoc

## Backend API Overview

The current backend exposes these main job-related routes:

- POST /api/jobs/scrape/ - scrape jobs from selected sources
- GET /api/jobs/ - list jobs with pagination
- GET /api/jobs/search/ - search jobs by keyword, location, or source
- GET /api/jobs/tracked/ - list tracked jobs
- GET /api/jobs/untracked/ - list untracked jobs
- POST /api/jobs/{job_id}/track/ - mark a job as tracked
- POST /api/jobs/{job_id}/untrack/ - mark a job as untracked
- POST /api/jobs/{job_id}/toggle/ - toggle tracking state

## Environment Variables

You can optionally create a .env file in the backend folder with values such as:

```env
DATABASE_URL=sqlite:///./job_tracker.db
SECRET_KEY=your-secret-key-here
INDEED_BASE_URL=https://www.indeed.com
LINKEDIN_BASE_URL=https://www.linkedin.com/jobs
SCHEDULER_ENABLED=true
SCRAPE_INTERVAL_HOURS=6
MAX_JOBS_PER_SEARCH=50
SCRAPE_DELAY_SECONDS=2
```

## Notes

- The backend is the main working part of the project right now.
- The Android folder is reserved for future mobile app development.
- For more detailed usage instructions, see [backend/QUICK_START.md](backend/QUICK_START.md).

## 🚨 Important Notes

* **Rate Limiting**: The scrapers include delays to respect website terms of service
* **Browser Requirements**: Playwright requires system dependencies for browser automation
* **Data Privacy**: All data is stored locally in SQLite database
* **Legal Compliance**: Ensure compliance with website terms of service when scraping

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For issues and questions:

1. Check the API documentation at `/docs`
2. Review the logs for error messages
3. Ensure all dependencies are installed
4. Verify Playwright browsers are installed

## 🔄 Updates

* **v2.0.0**: Enhanced with Playwright scraping, scheduling, and multi-source support
* **v1.0.0**: Initial release with basic job tracking
