CREATE TABLE IF NOT EXISTS service_requests (
    request_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_name TEXT NOT NULL,
    student_email TEXT NOT NULL,
    category TEXT NOT NULL,
    priority TEXT NOT NULL,
    estimate_hours REAL NOT NULL,
    status TEXT NOT NULL,
    created_date TEXT NOT NULL
);
