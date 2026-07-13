#!/usr/bin/env python3
"""
Setup script for Enhanced Job Tracker
Installs dependencies and sets up Playwright browsers
"""

import shutil
import subprocess
import sys


def run_command(command, description):
    """Run a command and handle errors."""
    print(f"Running: {description}")
    try:
        subprocess.run(command, check=True, capture_output=True, text=True)
        print(f"✓ {description} completed successfully")
        return True
    except subprocess.CalledProcessError as e:
        print(f"✗ {description} failed: {e}")
        if e.stderr:
            print(f"Error output: {e.stderr}")
        return False


def find_python_executable():
    """Prefer a Python version that is compatible with the pinned dependencies."""
    preferred_versions = ["3.11", "3.10", "3.9", "3.12"]
    for version in preferred_versions:
        candidate = shutil.which(f"python{version}")
        if candidate:
            return candidate

    # Fall back to the current interpreter
    return sys.executable


def main():
    print("Setting up Enhanced Job Tracker...")

    python_executable = find_python_executable()
    print(f"Using Python: {python_executable}")

    # Check if we're in a virtual environment
    if hasattr(sys, "real_prefix") or (hasattr(sys, "base_prefix") and sys.base_prefix != sys.prefix):
        print("✓ Running inside a virtual environment")
    else:
        print("⚠ Warning: It's recommended to run this in a virtual environment")

    current_version = sys.version_info[:2]
    if current_version >= (3, 14):
        print("⚠ Python 3.14 is not a good fit for the pinned dependencies in this project.")
        print("   Please use Python 3.11 or 3.10 for the best compatibility.")
        print("   On macOS, you can install it with: brew install python@3.11")
        print("   Then recreate your virtual environment with that interpreter.")

    # Install Python dependencies
    if not run_command(
        [python_executable, "-m", "pip", "install", "-r", "requirements.txt"],
        "Installing Python dependencies"
    ):
        print("Failed to install dependencies. Please check your Python environment.")
        return False

    # Install Playwright browsers
    if not run_command(
        [python_executable, "-m", "playwright", "install"],
        "Installing Playwright browsers"
    ):
        print("Failed to install Playwright browsers.")
        return False

    # Install Chromium specifically (most commonly used)
    if not run_command(
        [python_executable, "-m", "playwright", "install", "chromium"],
        "Installing Chromium browser"
    ):
        print("Failed to install Chromium browser.")
        return False

    print("\nSetup completed successfully!")
    print("\nNext steps:")
    print("1. Activate your virtual environment: source venv/bin/activate (macOS/Linux)")
    print("2. Run the application: python main.py")
    print("3. Open your browser and go to: http://localhost:8000/docs")
    print("\n🎯 Available features:")
    print("- Multi-source job scraping (Indeed, LinkedIn)")
    print("- Playwright-based web scraping")
    print("- Job tracking and application management")
    print("- Scheduled job scraping")
    print("- RESTful API with comprehensive endpoints")

    return True

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1) 