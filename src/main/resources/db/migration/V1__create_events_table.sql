CREATE TABLE IF NOT EXISTS events (
    event_id VARCHAR(36) PRIMARY KEY,
    event_name VARCHAR(36),
    event_time TIMESTAMPTZ NOT NULL,
    reminder_duration_in_secs INT
);