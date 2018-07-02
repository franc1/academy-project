insert into users(first_name, last_name, address, age, username, password, email) values
('Fran', 'Camaj', 'rr1', 30, 'frenki', '$2a$04$yFze2q5g8bRj2pjAZGURs.C/1XskMJKDihq9pOv4ZkKQ7tl3sfo1G', 'frenkicamaj@hotmail.com');
insert into user_roles(user_id, role_id) values ((select id from users where username='frenki'), (select id from roles where name='ROLE_USER'));
insert into users(first_name, last_name, address, age, username, password, email) values
('Michael', 'Henriks', '5th street', 45, 'miki', '$2a$04$Rk7uZmTpRuTfrCHJIW6N7uTVGhLkgrah/gfdA01d8tys6e89PN8ey', 'mikii@gmail.com');
insert into user_roles(user_id, role_id) values ((select id from users where username='miki'), (select id from roles where name='ROLE_USER'));
insert into users(first_name, last_name, address, age, username, password, email) values
('Ognjen', 'Markovic', 'bb', 23, 'ogi', '$2a$04$lSH/GD7zFNqyOkwmBdof3OHi2Mf0cA7M2.Se55Aw.YylyqdshzIAy', 'ogyya@yahoo.com');
insert into user_roles(user_id, role_id) values ((select id from users where username='ogi'), (select id from roles where name='ROLE_USER'));