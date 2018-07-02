INSERT INTO articles (title, employee_id, published_at)
VALUES 
	('JPQL Expression Language', (SELECT id FROM employees WHERE user_id = 1 LIMIT 1), NOW()),
	('Spring Boot Validators', (SELECT id FROM employees WHERE user_id = 1 LIMIT 1), '2018-06-20 10:00:00'),
	('Structural Patterns', (SELECT id FROM employees WHERE user_id = 1 LIMIT 1), '2018-06-20 17:00:00');