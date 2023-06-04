# This file is used to create the database and the tables
CREATE TABLE categories (
    name VARCHAR(255) NOT NULL PRIMARY KEY,
    color varchar(255)
);

CREATE TABLE entries (
                         id SERIAL PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         description VARCHAR(255),
                         category_name VARCHAR(255),
                         start_date TIMESTAMP NOT NULL,
                         end_date TIMESTAMP NOT NULL,
                         correction INT,
                         linked_id VARCHAR(255),
                         total INT
);

ALTER TABLE entries
    ADD CONSTRAINT fk_category FOREIGN KEY (category_name) REFERENCES categories(name);