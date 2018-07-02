package com.logate.academy.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.logate.academy.domains.Article;
import com.logate.academy.domains.Comment;
import com.logate.academy.domains.Employee;
import com.logate.academy.domains.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	
	public List<Comment> findByArticle(Article article);

	public List<Comment> findByArticleAndUser(Article article, User user);

	public List<Comment> findByPublishedAtAndUser(Date date, User user);

	public List<Comment> findByUser(User user);

	
	@Query(value = "select * from comments order by likes desc limit 5;", nativeQuery = true)
	public List<Object[]> findTopFive();

	@Query(value = "select * from comments order by dislikes desc limit 5;", nativeQuery = true)
	public List<Object[]> findWorstFive();
}
