CREATE TABLE seats (
    id SERIAL PRIMARY KEY,
    theater_id INTEGER NOT NULL,
    seat_row VARCHAR(2) NOT NULL,
    seat_number INTEGER NOT NULL,

    CONSTRAINT fk_theater_seats
        FOREIGN KEY (theater_id)
        REFERENCES theater(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_seat_per_theater
        UNIQUE (theater_id, seat_row, seat_number)
);

ALTER TABLE reserved_seats
    ADD COLUMN seat_id INTEGER NOT NULL;

ALTER TABLE reserved_seats
    ADD CONSTRAINT fk_reserved_seat_physical_seat
    FOREIGN KEY (seat_id)
    REFERENCES seats(id)
    ON DELETE RESTRICT;

ALTER TABLE reserved_seats
    DROP COLUMN seat_row,
    DROP COLUMN seat_number;