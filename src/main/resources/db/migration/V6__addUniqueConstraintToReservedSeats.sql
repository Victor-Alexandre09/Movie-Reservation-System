ALTER TABLE reserved_seats
ADD CONSTRAINT unique_reservation_per_seat_and_showtime 
UNIQUE (showtime_id, seat_id);