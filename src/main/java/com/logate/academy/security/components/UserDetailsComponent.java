package com.logate.academy.security.components;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.logate.academy.domains.User;
import com.logate.academy.repository.UserRepository;

@Component
public class UserDetailsComponent implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
        String lowerUsername = username.toLowerCase();
        Optional<User> optionalUser = userRepository.findByUsername(lowerUsername);
        
        if (optionalUser.isPresent())
        {
	        	User dbUser = optionalUser.get();
	        	if (dbUser.getIsActive() == null || !dbUser.getIsActive()) {
	        		throw new UsernameNotFoundException("User is not active!");
	        	}
	        	
	        	List<GrantedAuthority> grantedAuthorities = dbUser.getRoles()
	            		.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
	
            return new org.springframework.security.core.userdetails.User(
            		lowerUsername,
            		dbUser.getPassword(),
                grantedAuthorities
            );
        }
        throw new UsernameNotFoundException("User not exists in database!");
	}

}
