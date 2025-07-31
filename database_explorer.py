#!/usr/bin/env python3
"""
SQLite Database Explorer for Job Tracker - Simplified
This script helps you explore and interact with your job tracker database
"""

import sqlite3
import json
from datetime import datetime

def connect_db():
    """Connect to the SQLite database."""
    return sqlite3.connect('job_tracker.db')

def show_tables():
    """Show all tables in the database."""
    conn = connect_db()
    cursor = conn.cursor()
    
    print("📋 Database Tables:")
    cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
    tables = cursor.fetchall()
    
    for table in tables:
        print(f"  - {table[0]}")
    
    conn.close()

def show_jobs(limit=10, tracked_only=False, untracked_only=False):
    """Show jobs from the database."""
    conn = connect_db()
    cursor = conn.cursor()
    
    query = """
        SELECT id, title, company, location, source, is_tracked, created_at 
        FROM job 
        WHERE 1=1
    """
    params = []
    
    if tracked_only:
        query += " AND is_tracked = 1"
    elif untracked_only:
        query += " AND is_tracked = 0"
    
    query += " ORDER BY created_at DESC LIMIT ?"
    params.append(limit)
    
    cursor.execute(query, params)
    jobs = cursor.fetchall()
    
    filter_text = ""
    if tracked_only:
        filter_text = " (tracked only)"
    elif untracked_only:
        filter_text = " (untracked only)"
    
    print(f"\n💼 Jobs{filter_text} (showing first {limit}):")
    
    if not jobs:
        print("  No jobs found in database.")
    else:
        print(f"{'ID':<3} {'Title':<30} {'Company':<20} {'Location':<15} {'Source':<10} {'Tracked':<8}")
        print("-" * 90)
        for job in jobs:
            tracked = "✓" if job[5] else "✗"
            print(f"{job[0]:<3} {job[1][:28]:<30} {job[2][:18]:<20} {job[3][:13]:<15} {job[4]:<10} {tracked:<8}")
    
    conn.close()

def search_jobs(keyword=None, location=None, source=None, tracked_only=False):
    """Search jobs with filters."""
    conn = connect_db()
    cursor = conn.cursor()
    
    query = "SELECT id, title, company, location, source, is_tracked FROM job WHERE 1=1"
    params = []
    
    if keyword:
        query += " AND (title LIKE ? OR description LIKE ?)"
        params.extend([f"%{keyword}%", f"%{keyword}%"])
    
    if location:
        query += " AND location LIKE ?"
        params.append(f"%{location}%")
    
    if source:
        query += " AND source = ?"
        params.append(source)
    
    if tracked_only:
        query += " AND is_tracked = 1"
    
    query += " ORDER BY created_at DESC LIMIT 20"
    
    cursor.execute(query, params)
    jobs = cursor.fetchall()
    
    filter_text = " (tracked only)" if tracked_only else ""
    print(f"\n🔍 Search Results{filter_text} ({len(jobs)} jobs found):")
    if jobs:
        print(f"{'ID':<3} {'Title':<30} {'Company':<20} {'Location':<15} {'Source':<10} {'Tracked':<8}")
        print("-" * 85)
        for job in jobs:
            tracked = "✓" if job[5] else "✗"
            print(f"{job[0]:<3} {job[1][:28]:<30} {job[2][:18]:<20} {job[3][:13]:<15} {job[4]:<10} {tracked:<8}")
    else:
        print("  No jobs found matching your criteria.")
    
    conn.close()

def get_database_stats():
    """Show database statistics."""
    conn = connect_db()
    cursor = conn.cursor()
    
    print("\n📊 Database Statistics:")
    
    # Count jobs
    cursor.execute("SELECT COUNT(*) FROM job")
    job_count = cursor.fetchone()[0]
    print(f"  Total Jobs: {job_count}")
    
    # Count tracked jobs
    cursor.execute("SELECT COUNT(*) FROM job WHERE is_tracked = 1")
    tracked_count = cursor.fetchone()[0]
    print(f"  Tracked Jobs: {tracked_count}")
    
    # Count untracked jobs
    cursor.execute("SELECT COUNT(*) FROM job WHERE is_tracked = 0")
    untracked_count = cursor.fetchone()[0]
    print(f"  Untracked Jobs: {untracked_count}")
    
    # Jobs by source
    cursor.execute("SELECT source, COUNT(*) FROM job GROUP BY source")
    source_counts = cursor.fetchall()
    print("  Jobs by Source:")
    for source, count in source_counts:
        print(f"    {source}: {count}")
    
    # Recent jobs
    cursor.execute("SELECT COUNT(*) FROM job WHERE created_at >= datetime('now', '-7 days')")
    recent_count = cursor.fetchone()[0]
    print(f"  Jobs added in last 7 days: {recent_count}")
    
    conn.close()

def toggle_job_tracking(job_id):
    """Toggle job tracking status."""
    conn = connect_db()
    cursor = conn.cursor()
    
    try:
        # Get current status
        cursor.execute("SELECT is_tracked FROM job WHERE id = ?", (job_id,))
        result = cursor.fetchone()
        
        if not result:
            print(f"❌ Job with ID {job_id} not found.")
            return
        
        current_status = result[0]
        new_status = 0 if current_status else 1
        
        # Update status
        cursor.execute("UPDATE job SET is_tracked = ?, updated_at = ? WHERE id = ?", 
                      (new_status, datetime.now(), job_id))
        conn.commit()
        
        status_text = "tracked" if new_status else "untracked"
        print(f"✅ Job {job_id} marked as {status_text}")
        
    except sqlite3.Error as e:
        print(f"❌ Database error: {e}")
    finally:
        conn.close()

def run_custom_query():
    """Run a custom SQL query."""
    print("\n🔧 Custom SQL Query")
    print("Enter your SQL query (or 'exit' to quit):")
    
    conn = connect_db()
    cursor = conn.cursor()
    
    while True:
        try:
            query = input("\nSQL> ").strip()
            
            if query.lower() in ['exit', 'quit', 'q']:
                break
            
            if not query:
                continue
            
            cursor.execute(query)
            
            # Check if it's a SELECT query
            if query.strip().upper().startswith('SELECT'):
                results = cursor.fetchall()
                if results:
                    print(f"\nResults ({len(results)} rows):")
                    for i, row in enumerate(results[:10]):  # Show first 10 rows
                        print(f"  {i+1}: {row}")
                    if len(results) > 10:
                        print(f"  ... and {len(results) - 10} more rows")
                else:
                    print("  No results found.")
            else:
                conn.commit()
                print("  Query executed successfully.")
                
        except sqlite3.Error as e:
            print(f"  SQL Error: {e}")
        except KeyboardInterrupt:
            break
    
    conn.close()

def main():
    """Main menu for database explorer."""
    while True:
        print("\n" + "="*50)
        print("🗄️  Job Tracker Database Explorer - Simplified")
        print("="*50)
        print("1. Show database tables")
        print("2. Show all jobs")
        print("3. Show tracked jobs only")
        print("4. Show untracked jobs only")
        print("5. Search jobs")
        print("6. Search tracked jobs only")
        print("7. Toggle job tracking")
        print("8. Database statistics")
        print("9. Run custom SQL query")
        print("10. Exit")
        
        choice = input("\nEnter your choice (1-10): ").strip()
        
        if choice == '1':
            show_tables()
        elif choice == '2':
            limit = input("How many jobs to show? (default 10): ").strip()
            limit = int(limit) if limit.isdigit() else 10
            show_jobs(limit)
        elif choice == '3':
            limit = input("How many tracked jobs to show? (default 10): ").strip()
            limit = int(limit) if limit.isdigit() else 10
            show_jobs(limit, tracked_only=True)
        elif choice == '4':
            limit = input("How many untracked jobs to show? (default 10): ").strip()
            limit = int(limit) if limit.isdigit() else 10
            show_jobs(limit, untracked_only=True)
        elif choice == '5':
            keyword = input("Search keyword (optional): ").strip() or None
            location = input("Search location (optional): ").strip() or None
            source = input("Search source (optional): ").strip() or None
            search_jobs(keyword, location, source)
        elif choice == '6':
            keyword = input("Search keyword (optional): ").strip() or None
            location = input("Search location (optional): ").strip() or None
            source = input("Search source (optional): ").strip() or None
            search_jobs(keyword, location, source, tracked_only=True)
        elif choice == '7':
            job_id = input("Enter job ID to toggle tracking: ").strip()
            if job_id.isdigit():
                toggle_job_tracking(int(job_id))
            else:
                print("❌ Please enter a valid job ID (number).")
        elif choice == '8':
            get_database_stats()
        elif choice == '9':
            run_custom_query()
        elif choice == '10':
            print("Goodbye! 👋")
            break
        else:
            print("Invalid choice. Please try again.")

if __name__ == "__main__":
    main() 