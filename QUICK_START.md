# 🚀 Quick Start Guide - Job Tracker

## What is this?

This is a job tracking system that helps you:
1. **Find jobs** - Search for jobs by keyword and location
2. **Track jobs** - Save jobs you're interested in
3. **Track applications** - Keep track of your job applications and their status

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

### Step 7: Create an Application ⚠️ IMPORTANT

**⚠️ CRITICAL: You MUST use a valid job_id (number) that exists in your database!**

When you apply for a job, create an application record:

#### Option A: Web Interface (Recommended)
1. Find `POST /api/applications/`
2. Click "Try it out"
3. **Use this EXACT format**:
```json
{
  "job_id": 1,
  "status": "applied",
  "notes": "Applied via company website",
  "contact_person": "John Doe",
  "contact_email": "john@company.com"
}
```
4. Click "Execute"

#### Option B: PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8000/api/applications/" -Method POST -ContentType "application/json" -Body '{
  "job_id": 1,
  "status": "applied",
  "notes": "Applied via company website"
}'
```

### Step 8: Update Application Status

When you get updates on your application:

#### Option A: Web Interface
1. Find `PUT /api/applications/{application_id}`
2. Enter the application ID
3. Update the status:
```json
{
  "status": "interviewed",
  "notes": "First interview completed",
  "interview_date": "2024-01-20T10:00:00"
}
```

#### Option B: PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8000/api/applications/1" -Method PUT -ContentType "application/json" -Body '{
  "status": "interviewed",
  "notes": "First interview completed"
}'
```

## 🚨 Common Errors & Solutions

### Error: 422 Unprocessable Entity
**Cause**: Invalid data format when creating applications

**Solutions**:
1. **job_id must be a number**: `"job_id": 1` ✅ (not `"job_id": "1"` ❌)
2. **job_id must exist**: Make sure the job ID exists in your database
3. **Use correct JSON format**: All strings must be in quotes

**Example of CORRECT format**:
```json
{
  "job_id": 1,
  "status": "applied",
  "notes": "Applied via company website"
}
```

**Example of WRONG format**:
```json
{
  "job_id": "1",  // ❌ String instead of number
  "status": applied,  // ❌ Missing quotes
  "notes": "Applied via company website"
}
```

## 📊 Available Statuses

For applications, you can use these statuses:
- `applied` - You've applied
- `interviewed` - You've had an interview
- `offered` - You got a job offer
- `rejected` - You were rejected
- `withdrawn` - You withdrew your application

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


## 🎯 Example Workflow

1. **Search for jobs**: `POST /api/jobs/scrape/?keyword=python&location=remote`
2. **View results**: `GET /api/jobs/`
3. **Track interesting job**: `POST /api/jobs/1/track/`
4. **Apply for job**: Create application via `POST /api/applications/` with valid job_id
5. **Update status**: `PUT /api/applications/1` when you get updates

## 🆘 Need Help?

- **API Documentation**: http://localhost:8000/docs
- **Alternative Docs**: http://localhost:8000/redoc
- **Main Page**: http://localhost:8000/

## 💡 Tips

- **Use the web interface** (docs) - it's much easier than command line
- **Always check job IDs** before creating applications
- **Use valid job_id numbers** (not strings)
- **Update application status** regularly
- **Use the search function** to find specific jobs
- **Set up scheduled scraping** for automatic job discovery 