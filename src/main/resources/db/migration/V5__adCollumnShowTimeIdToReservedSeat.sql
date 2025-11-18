ALTER TABLE reserved_seats
    ADD COLUMN showtime_id INTEGER NOT NULL,

    ADD CONSTRAINT fk_showtime
    FOREIGN KEY (showtime_id)
    REFERENCES show_times(id)