package com.logate.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.logate.academy.domains.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> { }
