from fastapi import APIRouter, Depends, HTTPException, Query
from sqlmodel import Session
from typing import List, Optional
from backend.app.database import get_session
from backend.app.services.job_service import JobService
from backend.app.schemas.job import Job, JobCreate, JobUpdate, JobSearch

router = APIRouter()

@router.post("/scrape/")
async def scrape_jobs(
    keyword: str = Query(..., description="Job keyword to search for"),
    location: str = Query(..., description="Location to search in"),
    sources: Optional[List[str]] = Query(["indeed", "linkedin"], description="Job sources to scrape from"),
    db: Session = Depends(get_session)
):
    """Scrape jobs from multiple sources based on keyword and location."""
    job_service = JobService(db)
    try:
        jobs = await job_service.scrape_and_save_jobs(keyword, location, sources)
        return {
            "message": f"Found and saved {len(jobs)} jobs",
            "jobs": [job.dict() for job in jobs],
            "keyword": keyword,
            "location": location,
            "sources": sources
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error scraping jobs: {str(e)}")

@router.get("/", response_model=List[Job])
def get_jobs(
    skip: int = Query(0, ge=0, description="Number of records to skip"),
    limit: int = Query(100, ge=1, le=1000, description="Number of records to return"),
    db: Session = Depends(get_session)
):
    """Get all jobs with pagination."""
    job_service = JobService(db)
    return job_service.get_jobs(skip=skip, limit=limit)

@router.get("/search/")
def search_jobs(
    keyword: Optional[str] = Query(None, description="Search keyword"),
    location: Optional[str] = Query(None, description="Location filter"),
    source: Optional[str] = Query(None, description="Job source filter"),
    db: Session = Depends(get_session)
):
    """Search jobs by keyword, location, or source."""
    job_service = JobService(db)
    jobs = job_service.search_jobs(keyword=keyword, location=location, source=source)
    return {"jobs": [job.dict() for job in jobs], "count": len(jobs)}

@router.get("/tracked/", response_model=List[Job])
def get_tracked_jobs(db: Session = Depends(get_session)):
    """Get all tracked jobs."""
    job_service = JobService(db)
    return job_service.get_tracked_jobs()

@router.get("/untracked/", response_model=List[Job])
def get_untracked_jobs(db: Session = Depends(get_session)):
    """Get all untracked jobs."""
    job_service = JobService(db)
    return job_service.get_untracked_jobs()

@router.post("/{job_id}/track/")
def track_job(job_id: int, db: Session = Depends(get_session)):
    """Mark a job as tracked."""
    job_service = JobService(db)
    success = job_service.mark_job_as_tracked(job_id)
    if not success:
        raise HTTPException(status_code=404, detail="Job not found")
    return {"message": "Job marked as tracked successfully"}

@router.post("/{job_id}/untrack/")
def untrack_job(job_id: int, db: Session = Depends(get_session)):
    """Mark a job as untracked."""
    job_service = JobService(db)
    success = job_service.mark_job_as_untracked(job_id)
    if not success:
        raise HTTPException(status_code=404, detail="Job not found")
    return {"message": "Job marked as untracked successfully"}

@router.post("/{job_id}/toggle/")
def toggle_job_tracking(job_id: int, db: Session = Depends(get_session)):
    """Toggle job tracking status (tracked ↔ untracked)."""
    job_service = JobService(db)
    success = job_service.toggle_job_tracking(job_id)
    if not success:
        raise HTTPException(status_code=404, detail="Job not found")
    
    # Get the updated job to return the new status
    job = job_service.get_job(job_id)
    status = "tracked" if job.is_tracked else "untracked"
    return {"message": f"Job {status} successfully", "is_tracked": job.is_tracked}

@router.get("/{job_id}", response_model=Job)
def get_job(job_id: int, db: Session = Depends(get_session)):
    """Get a specific job by ID."""
    job_service = JobService(db)
    job = job_service.get_job(job_id)
    if job is None:
        raise HTTPException(status_code=404, detail="Job not found")
    return job

@router.post("/", response_model=Job)
def create_job(job: JobCreate, db: Session = Depends(get_session)):
    """Create a new job manually."""
    job_service = JobService(db)
    return job_service.create_job(job)

@router.put("/{job_id}", response_model=Job)
def update_job(job_id: int, job: JobUpdate, db: Session = Depends(get_session)):
    """Update a job."""
    job_service = JobService(db)
    updated_job = job_service.update_job(job_id, job)
    if updated_job is None:
        raise HTTPException(status_code=404, detail="Job not found")
    return updated_job

@router.delete("/{job_id}")
def delete_job(job_id: int, db: Session = Depends(get_session)):
    """Delete a job."""
    job_service = JobService(db)
    success = job_service.delete_job(job_id)
    if not success:
        raise HTTPException(status_code=404, detail="Job not found")
    return {"message": "Job deleted successfully"}
