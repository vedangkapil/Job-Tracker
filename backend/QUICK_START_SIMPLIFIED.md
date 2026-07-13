# 🚀 Quick Start Guide - Job Tracker (Simplified)

## What is this?

This is a **simplified job tracking system** that helps you:
1. **Find jobs** - Search for jobs by keyword and location
2. **Track jobs** - Save jobs you're interested in
3. **Manage tracked jobs** - View and organize your tracked jobs

## 🎯 How to Use It (Step by Step)

### Step 1: Start the Application
The application is already running at: **http://localhost:8000**

### Step 2: Open the API Documentation
Go to: **http://localhost:8000/docs**

This opens an interactive interface where you can test all the features.

### Step 3: Find Jobs

#### Option A: Using the Web Interface
1. Go to http://localhost:8000/docs
2. Find the section called "jobs"
3. Click on `POST /api/jobs/scrape/`
4. Click "Try it out"
5. Enter:
   - `keyword`: "python developer" (or any job title)
   - `location`: "remote" (or any location)
   - `sources`: ["indeed", "linkedin"]
6. Click "Execute"

#### Option B: Using PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/scrape/?keyword=python&location=remote" -Method POST
```

### Step 4: View Found Jobs

#### Option A: Web Interface
1. In the docs, find `GET /api/jobs/`
2. Click "Try it out" then "Execute"

#### Option B: PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/" -Method GET
```

### Step 5: Track a Job You Like

When you see a job you're interested in, mark it as tracked:

#### Option A: Web Interface
1. Find `POST /api/jobs/{job_id}/track/`
2. Enter the job ID (from the job list)
3. Click "Execute"

#### Option B: PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/1/track/" -Method POST
```

### Step 6: View Your Tracked Jobs

#### Option A: Web Interface
1. Find `GET /api/jobs/tracked/`
2. Click "Execute"

#### Option B: PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/tracked/" -Method GET
```

### Step 7: View Untracked Jobs

#### Option A: Web Interface
1. Find `GET /api/jobs/untracked/`
2. Click "Execute"

#### Option B: PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/untracked/" -Method GET
```

### Step 8: Toggle Job Tracking

Quickly switch a job between tracked and untracked:

#### Option A: Web Interface
1. Find `POST /api/jobs/{job_id}/toggle/`
2. Enter the job ID
3. Click "Execute"

#### Option B: PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/1/toggle/" -Method POST
```

## 📊 Available API Endpoints

### Jobs API (`/api/jobs/`)
- `POST /scrape/` - Scrape jobs from multiple sources
- `GET /` - Get all jobs with pagination
- `GET /search/` - Search jobs by keyword/location/source
- `GET /tracked/` - Get tracked jobs only
- `GET /untracked/` - Get untracked jobs only
- `POST /{id}/track/` - Mark job as tracked
- `POST /{id}/untrack/` - Mark job as untracked
- `POST /{id}/toggle/` - Toggle job tracking status
- `GET /{id}` - Get specific job
- `POST /` - Create job manually
- `PUT /{id}` - Update job
- `DELETE /{id}` - Delete job

## 🔍 Search Jobs

You can search for specific jobs:

```powershell
# Search by keyword
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/search/?keyword=python" -Method GET

# Search by location
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/search/?location=remote" -Method GET

# Search by source
Invoke-WebRequest -Uri "http://localhost:8000/api/jobs/search/?source=indeed" -Method GET
```

## 🗄️ Database Explorer

Use the database explorer to interact with your data directly:

```powershell
python database_explorer.py
```

This gives you:
- View all jobs
- View tracked/untracked jobs only
- Search jobs with filters
- Toggle job tracking
- Database statistics
- Run custom SQL queries

## 🎯 Example Workflow

1. **Search for jobs**: `POST /api/jobs/scrape/?keyword=python&location=remote`
2. **View results**: `GET /api/jobs/`
3. **Track interesting job**: `POST /api/jobs/1/track/`
4. **View tracked jobs**: `GET /api/jobs/tracked/`
5. **Toggle tracking**: `POST /api/jobs/1/toggle/` to untrack

## 🆘 Need Help?

- **API Documentation**: http://localhost:8000/docs
- **Alternative Docs**: http://localhost:8000/redoc
- **Main Page**: http://localhost:8000/
- **Database Explorer**: `python database_explorer.py`

## 💡 Tips

- **Use the web interface** (docs) - it's much easier than command line
- **Track jobs you're interested in** - use the track/toggle features
- **Search effectively** - use keywords, locations, and sources
- **Use the database explorer** - for direct data access and management
- **Keep it simple** - focus on finding and tracking jobs that interest you

## 🎉 What's Different in This Simplified Version?

✅ **Kept:**
- Job scraping from Indeed and LinkedIn
- Job listing and viewing
- Job tracking (tracked/untracked)
- Job search and filtering
- Database for jobs

❌ **Removed:**
- Application management
- Scheduler functionality
- Complex application tracking

This simplified version focuses purely on **job discovery and tracking** - perfect for finding and organizing jobs you're interested in! 🚀 