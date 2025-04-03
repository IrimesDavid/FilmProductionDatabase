DROP DATABASE IF EXISTS `filmproduction_database`;
CREATE DATABASE `filmproduction_database`;
USE `filmproduction_database`;


CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    baseType VARCHAR(50) NOT NULL,
    image VARCHAR(2048)
);

CREATE TABLE films (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    year INT NOT NULL, 
    type VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    director_id INT NOT NULL,
    writer_id INT NOT NULL,
    producer_id INT NOT NULL,
    FOREIGN KEY (director_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (writer_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (producer_id) REFERENCES members(id) ON DELETE CASCADE
);

CREATE TABLE casts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    film_id INT NOT NULL,
    actor_id INT NOT NULL,
    role VARCHAR(255) NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES members(id) ON DELETE CASCADE
);

CREATE TABLE filmimages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    film_id INT NOT NULL,
    url VARCHAR(2048),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE
);


INSERT INTO members (name, birthdate, baseType, image) VALUES
('Christopher Nolan', '1970-07-30', 'director', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Christopher-Nolan.jpg'),
('Quentin Tarantino', '1963-03-27', 'director', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Quentin-Tarantino.jpg'),
('Steven Spielberg', '1946-12-18', 'producer', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Steven-Spielberg.jpg'),
('Aaron Sorkin', '1961-06-19', 'writer', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Aaron-Sorkin.jpg'),
('Jamie Foxx', '1967-12-13', 'actor', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Jamie-Foxx.jpg'),
('Leonardo DiCaprio', '1974-11-11', 'actor', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Leonardo-DiCaprio.jpg'),
('Liam Neeson', '1963-12-18', 'actor', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Liam-Neeson.jpg'),
('Tom Hardy', '1977-09-15', 'actor', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Tom-Hardy.jpg'),
('Joseph Gordon-Levitt', '1981-02-17', 'actor', 'E:\\ANUL3\\PS\\Tema1\\Database\\MemberImages\\Joseph-Gordon-Levitt.jpg');


INSERT INTO films (title, year, type, category, director_id, writer_id, producer_id) VALUES
('Inception', 2010, 'Artistic', 'SciFi',
    (SELECT id FROM members WHERE name = 'Christopher Nolan'),
    (SELECT id FROM members WHERE name = 'Christopher Nolan'),
    (SELECT id FROM members WHERE name = 'Christopher Nolan')),

('Django Unchained', 2012, 'Artistic', 'Western',
    (SELECT id FROM members WHERE name = 'Quentin Tarantino'),
    (SELECT id FROM members WHERE name = 'Quentin Tarantino'),
    (SELECT id FROM members WHERE name = 'Quentin Tarantino')),

('Schindler''s List', 1993, 'Artistic', 'Historical',
    (SELECT id FROM members WHERE name = 'Steven Spielberg'),
    (SELECT id FROM members WHERE name = 'Aaron Sorkin'),
    (SELECT id FROM members WHERE name = 'Steven Spielberg'));


INSERT INTO casts (film_id, actor_id, role) VALUES
-- Inception cast
((SELECT id FROM films WHERE title = 'Inception'),
 (SELECT id FROM members WHERE name = 'Leonardo DiCaprio'),
 'Dominic Cobb'),
 
((SELECT id FROM films WHERE title = 'Inception'),
 (SELECT id FROM members WHERE name = 'Joseph Gordon-Levitt'),
 'Arthur'),

((SELECT id FROM films WHERE title = 'Inception'),
 (SELECT id FROM members WHERE name = 'Tom Hardy'),
 'Eames'),

-- Django Unchained cast
((SELECT id FROM films WHERE title = 'Django Unchained'),
 (SELECT id FROM members WHERE name = 'Leonardo DiCaprio'),
 'Calvin Candie'),

((SELECT id FROM films WHERE title = 'Django Unchained'),
 (SELECT id FROM members WHERE name = 'Jamie Foxx'),
 'Django'),

-- Schindler's List cast
((SELECT id FROM films WHERE title = 'Schindler''s List'),
 (SELECT id FROM members WHERE name = 'Liam Neeson'),
 'Oskar Schindler');


INSERT INTO filmimages (film_id, url) VALUES
-- Inception filmimages
((SELECT id FROM films WHERE title = 'Inception'), 'E:\\ANUL3\\PS\\Tema1\\Database\\FilmImages\\Inception1.jpg'),
((SELECT id FROM films WHERE title = 'Inception'), 'E:\\ANUL3\\PS\\Tema1\\Database\\FilmImages\\Inception2.jpg'),
((SELECT id FROM films WHERE title = 'Inception'), 'E:\\ANUL3\\PS\\Tema1\\Database\\FilmImages\\Inception3.jpg'),

-- Django Unchained filmimages
((SELECT id FROM films WHERE title = 'Django Unchained'), 'E:\\ANUL3\\PS\\Tema1\\Database\\FilmImages\\Django1.jpg'),
((SELECT id FROM films WHERE title = 'Django Unchained'), 'E:\\ANUL3\\PS\\Tema1\\Database\\FilmImages\\Django2.jpg'),

-- Schindler's List filmimages
((SELECT id FROM films WHERE title = 'Schindler''s List'), 'E:\\ANUL3\\PS\\Tema1\\Database\\FilmImages\\Schindler''s-List.jpg');
