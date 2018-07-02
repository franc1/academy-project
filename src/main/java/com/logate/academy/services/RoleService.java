package com.logate.academy.services;

import java.util.ArrayList;
import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.logate.academy.domains.Article;
import com.logate.academy.domains.Category;
import com.logate.academy.domains.Comment;
import com.logate.academy.domains.Role;
import com.logate.academy.domains.User;
import com.logate.academy.repository.CommentRepository;
import com.logate.academy.repository.RoleRepository;
import com.logate.academy.repository.UserRepository;
import com.logate.academy.web.dto.CommentDTO;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	public Role store(Role role) {
		return roleRepository.save(role);
	}
}
