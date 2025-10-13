    CREATE TABLE roles (
        id SERIAL PRIMARY KEY,
        name VARCHAR(50) NOT NULL UNIQUE
    );

    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        role_id INTEGER NOT NULL,
        created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

        CONSTRAINT fk_role
            FOREIGN KEY(role_id)
            REFERENCES roles(id)
            ON DELETE RESTRICT
    );

    CREATE TABLE genres (
        id SERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL UNIQUE
    );

    CREATE TABLE movies (
        id SERIAL PRIMARY KEY,
        title VARCHAR(100) NOT NULL,
        poster_image_url VARCHAR(255),
        duration_minutes INTEGER,
        release_date DATE
    );

    CREATE TABLE movies_genres (
        movie_id INTEGER NOT NULL,
        genres_id INTEGER NOT NULL,

        PRIMARY KEY (movie_id, genres_id),

        CONSTRAINT fk_movie
            FOREIGN KEY(movie_id)
            REFERENCES movies(id)
            ON DELETE CASCADE,

        CONSTRAINT fk_genre
            FOREIGN KEY(genres_id)
            REFERENCES genres(id)
            ON DELETE CASCADE
    );

    CREATE TABLE theater (
        id SERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        capacity INTEGER NOT NULL
    );

    CREATE TABLE show_times (
        id SERIAL PRIMARY KEY,
        theater_id INTEGER NOT NULL,
        movie_id INTEGER NOT NULL,
        start_time TIMESTAMP WITH TIME ZONE NOT NULL,
        price DECIMAL(10, 2) NOT NULL,

        CONSTRAINT fk_theater
            FOREIGN KEY(theater_id)
            REFERENCES theater(id)
            ON DELETE RESTRICT,

        CONSTRAINT fk_movie
            FOREIGN KEY(movie_id)
            REFERENCES movies(id)
            ON DELETE RESTRICT
    );

    CREATE TABLE reservations (
        id SERIAL PRIMARY KEY,
        showtime_id INTEGER NOT NULL,
        user_id INTEGER NOT NULL,
        status VARCHAR(50) DEFAULT 'confirmed' NOT NULL,
        total_price DECIMAL(10, 2) NOT NULL,
        created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

        CONSTRAINT fk_showtime
            FOREIGN KEY(showtime_id)
            REFERENCES show_times(id)
            ON DELETE RESTRICT,

        CONSTRAINT fk_user
            FOREIGN KEY(user_id)
            REFERENCES users(id)
            ON DELETE RESTRICT
    );

    CREATE TABLE reserved_seats (
        id SERIAL PRIMARY KEY,
        reservation_id INTEGER NOT NULL,
        seat_row VARCHAR(10) NOT NULL,
        seat_number INTEGER NOT NULL,
        created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

        CONSTRAINT fk_reservation
            FOREIGN KEY(reservation_id)
            REFERENCES reservations(id)
            ON DELETE CASCADE
    );