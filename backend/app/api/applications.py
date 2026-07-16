from typing import List

from fastapi import APIRouter, Depends
from sqlmodel import Session

from backend.app.database import get_session
from backend.app.schemas.application import Application
from backend.app.services.application_service import ApplicationService

router = APIRouter()


@router.get("", response_model=List[Application])
def get_applications(db: Session = Depends(get_session)):
    """Return the user's applications for the mobile application tracker."""
    return ApplicationService(db).get_applications()
