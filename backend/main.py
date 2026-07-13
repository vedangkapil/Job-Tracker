from fastapi import FastAPI
from app.api import jobs
from app.database import create_db_and_tables
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Create database tables
create_db_and_tables()

app = FastAPI(
    title="Job Tracker - Simplified",
    description="Job scraping and tracking system - focus on job discovery and tracking",
    version="2.1.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# Include only jobs router
app.include_router(jobs.router, prefix="/api/jobs", tags=["jobs"])

@app.get("/")
async def root():
    return {
        "message": "Welcome to Job Tracker - Simplified",
        "version": "2.1.0",
        "features": [
            "Multi-source job scraping (Indeed, LinkedIn)",
            "Job tracking and management",
            "Job search and filtering",
            "Simple and focused functionality"
        ],
        "docs": "/docs",
        "redoc": "/redoc"
    }

@app.on_event("startup")
async def startup_event():
    """Initialize the application on startup."""
    logger.info("Starting Job Tracker - Simplified...")

@app.on_event("shutdown")
async def shutdown_event():
    """Clean up on application shutdown."""
    logger.info("Shutting down Job Tracker - Simplified...")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)