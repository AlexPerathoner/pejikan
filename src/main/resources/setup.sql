# This file is used to create the database and the tables
CREATE TABLE categories (
    name VARCHAR(255) NOT NULL PRIMARY KEY,
    color varchar(255)
);

CREATE TABLE entries (
                         id SERIAL PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         description VARCHAR(255),
                         category VARCHAR(255),
                         start_date DATE NOT NULL,
                         end_date DATE NOT NULL,
                         correction TIME,
                         linked_id VARCHAR(255),
                         total TIME
);

ALTER TABLE entries
    ADD CONSTRAINT fk_category FOREIGN KEY (category) REFERENCES categories(name);