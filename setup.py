#!/usr/bin/env python3
"""
Setup script for Enhanced Job Tracker
Installs dependencies and sets up Playwright browsers
"""

import subprocess
import sys
import os

def run_command(command, description):
    """Run a command and handle errors."""
    print(f"Running: {description}")
    try:
        result = subprocess.run(command, shell=True, check=True, capture_output=True, text=True)
        print(f"✓ {description} completed successfully")
        return True
    except subprocess.CalledProcessError as e:
        print(f"✗ {description} failed: {e}")
        print(f"Error output: {e.stderr}")
        return False

def main():
    print("🚀 Setting up Enhanced Job Tracker...")
    
    # Check if we're in a virtual environment
    if not hasattr(sys, 'real_prefix') and not (hasattr(sys, 'base_prefix') and sys.base_prefix != sys.prefix):
        print("⚠️  Warning: It's recommended to run this in a virtual environment")
    
    # Install Python dependencies
    if not run_command("pip install -r requirements.txt", "Installing Python dependencies"):
        print("❌ Failed to install dependencies. Please check your Python environment.")
        return False
    
    # Install Playwright browsers
    if not run_command("playwright install", "Installing Playwright browsers"):
        print("❌ Failed to install Playwright browsers.")
        return False
    
    # Install Chromium specifically (most commonly used)
    if not run_command("playwright install chromium", "Installing Chromium browser"):
        print("❌ Failed to install Chromium browser.")
        return False
    
    print("\n✅ Setup completed successfully!")
    print("\n📋 Next steps:")
    print("1. Activate your virtual environment: venv\\Scripts\\activate (Windows) or source venv/bin/activate (Linux/Mac)")
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