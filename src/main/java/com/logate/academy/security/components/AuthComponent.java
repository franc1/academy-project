package com.logate.academy.security.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.logate.academy.config.MicroserviceConfiguration;
import com.logate.academy.domains.Comment;
import com.logate.academy.domains.Role;
import com.logate.academy.domains.User;
import com.logate.academy.repository.ArticleRepository;
import com.logate.academy.repository.CommentRepository;
import com.logate.academy.repository.UserRepository;
import com.logate.academy.web.dto.CommentDTO;

@Description(value = "Authentication Component.")
@Component(value = "authComponent")
public class AuthComponent {
	// njegove metode moraju vratiti boolean rezultat!
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthComponent.class);
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${microservice.key}")
	private String key;               // 1. nacin za uzimanje 'key' iz application.yml
	
	@Autowired
	private Environment environment;      // 2. nacin za uzimanje 'key' iz application.yml 
	
	@Autowired
	private MicroserviceConfiguration microConf;      // 3. nacin za uzimanje 'key' iz application.yml
	
	Authentication auth;
	
	/**
	 * Method for checking permission
	 * @return boolean value (has permission or not)
	 */
	public boolean hasPermission(CommentDTO commentDTO) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		List<Comment> comment = commentRepository.findByArticle(commentDTO.getArticle());
		
		for(Comment comm: comment) {
			if(comm.getUser().getUsername().equals(auth.getPrincipal().toString()))
				return true;
		}
		return false;
	}
	
	
	public boolean hasLess10(CommentDTO commentDTO) {
		Date dateTo = new Date(commentDTO.getPublishedAt().getTime());
		dateTo.setHours(23);
		dateTo.setMinutes(59);
		dateTo.setSeconds(59);
		Date dateFrom = new Date(commentDTO.getPublishedAt().getTime());
		dateFrom.setHours(0);
		dateFrom.setMinutes(0);
		dateFrom.setSeconds(0);

		auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(auth.getPrincipal().toString()).get();
		List<Comment> dbComment = commentRepository.findByArticleAndUser(commentDTO.getArticle(), user);
		List<Comment> comment = new ArrayList<>();
		for(Comment comm: dbComment) {
			if(comm.getPublishedAt().before(dateTo) && comm.getPublishedAt().after(dateFrom)) {
				comment.add(comm);
			}
		}
		
		if(comment.size()<10) {
			return true;
		}
		return false;
	}
	
	
	public boolean canDelete(Integer commentId) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(auth.getPrincipal().toString()).get();
		Optional<Comment> commentOpt = commentRepository.findById(commentId);
		if(commentOpt.isPresent()) {
			Comment comment = commentOpt.get();
			
			for(Role role: user.getRoles()) {
				if(role.getName().equals("ROLE_ADMIN"))
					return true;
			}
	
			if(comment.getUser() == user) {
				return true;
			}
			
			return false;
		}
		return true;
	}
	
	/**
	 * Method for checking Authorization header value
	 * @param authHeaderValue
	 * @return
	 */
	public boolean hasHeaderPermission(String authHeaderValue) {
		LOGGER.info("Header value: {}", authHeaderValue);
		if (authHeaderValue.trim().contentEquals(key)) {     // koristimo KEY iz  1. nacina...
			return true;
		}
		return false;
	}

}
