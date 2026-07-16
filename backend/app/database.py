from sqlmodel import SQLModel, create_engine, Session
from backend.app.config import settings
from backend.app.models.application import Application
from backend.app.models.job import Job

engine = create_engine(settings.database_url, echo=True)

def create_db_and_tables():
    """Create the tables used by job discovery and application tracking."""
    SQLModel.metadata.create_all(engine)

def get_session():
    with Session(engine) as session:
        yield session
