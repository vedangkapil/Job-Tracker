# Enhanced Job Scraper & Tracker

A comprehensive FastAPI application for scraping job listings from multiple sources and tracking job applications with advanced features.

Repository note: the implementation is organized under the `backend/` folder. Run the FastAPI backend from `backend/main.py` (or with `uvicorn backend.main:app --reload`). If you are following Quick Start instructions, prefer the files inside `backend/` (for example `backend/QUICK_START.md`). Mobile (Android) code, if present, lives in a separate folder to keep concerns separated.


## 🚀 Features

* **Multi-Source Job Scraping**: Scrape job listings from Indeed and LinkedIn using Playwright
* **Advanced Web Scraping**: Robust scraping with error handling, rate limiting, and data extraction
* **Job Tracking System**: Track job applications with detailed status management
* **Scheduled Scraping**: Automatically scrape jobs at configurable intervals using APScheduler
* **RESTful API**: Comprehensive API endpoints for all functionality
* **SQLite Database**: Local database with SQLModel for data persistence
* **Modern Tech Stack**: FastAPI, SQLModel, Playwright, APScheduler

## 📋 Project Structure

```text
/job-tracker
├── .env                  # Environment variables
├── requirements.txt      # Project dependencies
├── setup.py             # Setup script for Playwright
├── README.md            # Project documentation
├── main.py              # Application entry point
├── app/                 # Application package
│   ├── __init__.py
│   ├── config.py        # Configuration settings
│   ├── database.py      # Database connection
│   ├── models/          # SQLModel models
│   │   ├── __init__.py
│   │   ├── job.py       # Job model
│   │   └── application.py # Application model
│   ├── schemas/         # Pydantic schemas
│   │   ├── __init__.py
│   │   ├── job.py       # Job schemas
│   │   └── application.py # Application schemas
│   ├── scrapers/        # Web scrapers
│   │   ├── __init__.py
│   │   ├── indeed.py    # Indeed scraper
│   │   ├── linkedin.py  # LinkedIn scraper
│   │   └── manager.py   # Scraper manager
│   ├── api/             # API endpoints
│   │   ├── __init__.py
│   │   ├── jobs.py      # Job endpoints
│   │   ├── applications.py # Application endpoints
│   │   └── scheduler.py # Scheduler endpoints
│   └── services/        # Business logic
│       ├── __init__.py
│       ├── job_service.py # Job service
│       ├── application_service.py # Application service
│       └── scheduler_service.py # Scheduler service
```

## 🛠️ Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/job-tracker.git
cd job-tracker
```

### 2. Create Virtual Environment

**Windows**

```bash
python -m venv venv
```

**macOS/Linux**

```bash
python3 -m venv venv
```

### 3. Activate Virtual Environment

**Windows**

```bash
venv\Scripts\activate
```

**macOS/Linux**

```bash
source venv/bin/activate
```

### 4. Run Setup Script

**Windows**

```bash
python setup.py
```

**macOS/Linux**

```bash
python3 setup.py
```

This will:

* Install all Python dependencies
* Install Playwright browsers
* Set up the environment

### 5. Manual Installation (Alternative)

If you prefer manual installation:

**Windows**

```bash
# Install dependencies
pip install -r requirements.txt

# Install Playwright browsers
playwright install
playwright install chromium
```

**macOS/Linux**

```bash
# Install dependencies
pip3 install -r requirements.txt

# Install Playwright browsers
playwright install
playwright install chromium
```

### 6. Environment Configuration

Create a `.env` file in the root directory:

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

## 🚀 Running the Application

**Windows**

```bash
python main.py
```

**macOS/Linux**

```bash
python3 main.py
```

The API will be available at:

* **API**: http://localhost:8000
* **Swagger UI**: http://localhost:8000/docs
* **ReDoc**: http://localhost:8000/redoc

## 📚 API Usage

### Job Scraping

#### Scrape Jobs from Multiple Sources

```bash
POST /api/jobs/scrape/
{
  "keyword": "python developer",
  "location": "remote",
  "sources": ["indeed", "linkedin"]
}
```

#### Get All Jobs

```bash
GET /api/jobs/?skip=0&limit=100
```

#### Search Jobs

```bash
GET /api/jobs/search/?keyword=python&location=remote&source=indeed
```

#### Get Tracked Jobs

```bash
GET /api/jobs/tracked/
```

#### Mark Job as Tracked

```bash
POST /api/jobs/{job_id}/track/
```

### Application Tracking

#### Create Application

```bash
POST /api/applications/
{
  "job_id": 1,
  "status": "applied",
  "notes": "Applied via LinkedIn",
  "contact_person": "John Doe",
  "contact_email": "john@company.com"
}
```

#### Get Applications

```bash
GET /api/applications/
```

#### Get Applications by Status

```bash
GET /api/applications/status/applied/
```

#### Update Application

```bash
PUT /api/applications/{application_id}
{
  "status": "interviewed",
  "interview_date": "2024-01-20T10:00:00",
  "notes": "First interview completed"
}
```

### Scheduled Scraping

#### Add Scheduled Job

```bash
POST /api/scheduler/jobs/
{
  "keyword": "python developer",
  "location": "remote",
  "sources": ["indeed", "linkedin"],
  "interval_hours": 6
}
```

#### Start Scheduler

```bash
POST /api/scheduler/start/
```

#### Get Scheduler Status

```bash
GET /api/scheduler/status/
```

#### Get Scheduled Jobs

```bash
GET /api/scheduler/jobs/
```

## 🎯 Example Workflow

### 1. Initial Setup

**Windows**

```bash
python main.py
```

**macOS/Linux**

```bash
python3 main.py
```

Open http://localhost:8000/docs in your browser.

### 2. Scrape Jobs

```bash
curl -X POST "http://localhost:8000/api/jobs/scrape/?keyword=python%20developer&location=remote"
```

### 3. View Scraped Jobs

```bash
curl "http://localhost:8000/api/jobs/"
```

### 4. Track a Job

```bash
curl -X POST "http://localhost:8000/api/jobs/1/track/"
```

### 5. Create Application

```bash
curl -X POST "http://localhost:8000/api/applications/" \
  -H "Content-Type: application/json" \
  -d '{
    "job_id": 1,
    "status": "applied",
    "notes": "Applied via company website"
  }'
```

### 6. Set Up Scheduled Scraping

```bash
curl -X POST "http://localhost:8000/api/scheduler/jobs/" \
  -H "Content-Type: application/json" \
  -d '{
    "keyword": "python developer",
    "location": "remote",
    "interval_hours": 6
  }'

curl -X POST "http://localhost:8000/api/scheduler/start/"
```

## 🔧 Configuration

### Scraping Settings

* `MAX_JOBS_PER_SEARCH`: Maximum jobs to scrape per search (default: 50)
* `SCRAPE_DELAY_SECONDS`: Delay between job extractions (default: 2)
* `INDEED_BASE_URL`: Indeed base URL
* `LINKEDIN_BASE_URL`: LinkedIn jobs base URL

### Scheduler Settings

* `SCHEDULER_ENABLED`: Enable/disable scheduler (default: true)
* `SCRAPE_INTERVAL_HOURS`: Default interval for scheduled jobs (default: 6)

## 🛡️ Error Handling

The application includes comprehensive error handling:

* Network errors during scraping
* Database connection issues
* Invalid job/application data
* Scheduler failures
* Playwright browser issues

## 📊 Database Schema

### Jobs Table

* `id`: Primary key
* `title`: Job title
* `company`: Company name
* `location`: Job location
* `description`: Job description
* `salary`: Salary information
* `job_url`: Job posting URL
* `source`: Source (indeed, linkedin)
* `scraped_at`: When job was scraped
* `keywords`: Search keywords
* `is_tracked`: Whether job is being tracked
* `created_at`: Record creation time
* `updated_at`: Record update time

### Applications Table

* `id`: Primary key
* `job_id`: Foreign key to jobs table
* `status`: Application status
* `notes`: Application notes
* `applied_date`: When application was submitted
* `interview_date`: Interview date
* `follow_up_date`: Follow-up reminder date
* `contact_person`: Contact person name
* `contact_email`: Contact email
* `salary_offered`: Offered salary
* `created_at`: Record creation time
* `updated_at`: Record update time

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
