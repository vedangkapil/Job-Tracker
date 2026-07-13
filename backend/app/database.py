from sqlmodel import SQLModel, create_engine, Session
from backend.app.config import settings
from backend.app.models.job import Job  # Only import Job model

engine = create_engine(settings.database_url, echo=True)

def create_db_and_tables():
    """Create database tables - only jobs table for simplified version."""
    SQLModel.metadata.create_all(engine)

def get_session():
    with Session(engine) as session:
        yield session