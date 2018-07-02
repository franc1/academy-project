INSERT INTO comments (article_id, body, published_at)
VALUES 
((SELECT id FROM articles WHERE id = 1), 'Top post', '2018-06-10 00:00:00'),
((SELECT id FROM articles WHERE id = 1), 'Great!', '2018-06-10 12:00:00'),
((SELECT id FROM articles WHERE id = 1), 'Not bad...', '2018-06-10 14:56:00'),
((SELECT id FROM articles WHERE id = 2), 'Correct.', '2018-06-10 14:00:00');