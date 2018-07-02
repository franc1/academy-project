INSERT INTO employees (user_id, department_id, hire_date, job_description)
VALUES 
(
	(SELECT id FROM users WHERE first_name = 'Heril' LIMIT 1),
	(SELECT id FROM departments WHERE name = 'Space Cadets' LIMIT 1),
	DATE(NOW()),
	'Software Developer & Technical Lead'
),
(
	(SELECT id FROM users WHERE email = 'marko.andjelic@logate.com' LIMIT 1),
	(SELECT id FROM departments WHERE name = 'Space Cadets' LIMIT 1),
	DATE(NOW()),
	'Project Manager'
),
(
	(SELECT id FROM users WHERE email = 'marko.vujovic@logate.com' LIMIT 1),
	(SELECT id FROM departments WHERE name = 'Space Cadets' LIMIT 1),
	DATE(NOW()),
	'Software Developer'
);