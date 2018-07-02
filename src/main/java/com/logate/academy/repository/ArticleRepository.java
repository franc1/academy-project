package com.logate.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.logate.academy.domains.Article;
import com.logate.academy.domains.Employee;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	//@Query(value = "select * from articles order by published_at desc limit 5", nativeQuery = true)
	@Query(value = "select e from Article e order by e.publishedAt desc")
	List<Article> findLastFive();

	@Query(value = "select * from articles where "
			+ "id=(select article_id from comments group by article_id "
			+ "order by count(article_id) desc limit 1)", nativeQuery = true)
	List<Object[]> findTopFive(); 
	

	//e.id, u.title, e.employee_id, e.published_at, e.document_it, e.category_id
}
