package com.logate.academy.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.logate.academy.domains.Article;
import com.logate.academy.domains.Category;
import com.logate.academy.domains.Document;
import com.logate.academy.domains.Employee;
import com.logate.academy.repository.ArticleRepository;
import com.logate.academy.web.dto.ArticleDTO;
import com.logate.academy.web.dto.EmployeeDTO;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleRepository articleRepository;

	public Article store(Article article) {
		
		return articleRepository.save(article);
	}

	
	public void delete(Integer articleId) {

		articleRepository.deleteById(articleId);
	}

	
	public List<Article> getAllArticles() {
		return articleRepository.findAll();
	}


	public Optional<Article> findById(Integer articleId) {
		return articleRepository.findById(articleId);
	}


	public Article updateArticle(Article article) {
		Optional<Article> dbArticle = articleRepository.findById(article.getId());
		
		if(dbArticle.isPresent()) {
			return articleRepository.save(article);
		}
		return null;
	}


	public List<Article> findLastFive() {
		List<Article> lastArticles = articleRepository.findLastFive();
		
		return lastArticles.subList(0, 5);
	}


	public List<ArticleDTO> findTopFive() {

		List<Object[]> objectsList = articleRepository.findTopFive();
		List<ArticleDTO> topArticles = new ArrayList<>();
		
		// processing data...
		objectsList
			.stream()
			.forEach(articles -> {
				ArticleDTO articleDTO = new ArticleDTO(
					(Integer) articles[0],
					(String) articles[1],
					(Integer) articles[2],
					(Date) articles[3],
					(Integer) articles[4],
					(Integer) articles[5]
				);
				topArticles.add(articleDTO);
			});
		
		return topArticles;
	}


	
	
}
