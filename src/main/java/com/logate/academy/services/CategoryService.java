package com.logate.academy.services;

import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.logate.academy.domains.Article;
import com.logate.academy.domains.Category;
import com.logate.academy.domains.Comment;
import com.logate.academy.repository.CategoryRepository;
import com.logate.academy.repository.CommentRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	public Category store(Category category) {
		return categoryRepository.save(category);
	}

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Category updateCategory(Category category) {
		Optional<Category> dbCategory = categoryRepository.findById(category.getId());
		
		if(dbCategory.isPresent()) {
			return categoryRepository.save(category);
		}
		return null;
	}

	
}
