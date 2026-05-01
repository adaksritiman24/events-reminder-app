ALTER TABLE events
ADD COLUMN reminder_time TIMESTAMPTZ,
DROP COLUMN reminder_duration_in_secs;
