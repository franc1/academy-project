package com.logate.academy.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logate.academy.domains.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
}
