package com.logate.academy.services;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.logate.academy.domains.Role;
import com.logate.academy.domains.User;
import com.logate.academy.repository.RoleRepository;
import com.logate.academy.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Integer userId) {
		return userRepository.findById(userId);      //ovaj metod ne vraca User vec Optional<User>
	}

	public User updateUser(User user) {
		return userRepository.save(user);     //vraca unijetog(update-ovanog ili insertovanog) usera
	}

	public Page<User> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	
	
	//uzima user, zatim mu update-uje samo firstName i lastName...
	public User updateUser(User user, Integer id) 
	{
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User fetchedUser = userOptional.get();
			fetchedUser.setFirstName(user.getFirstName());
			fetchedUser.setLastName(user.getLastName());
			
			return userRepository.save(fetchedUser);
		}
		return null;
	}
	
	
	public User store(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role role = roleRepository.findByName("ROLE_USER");
		user.addRole(role);
		return userRepository.save(user);
	}

	public void delete(Integer id) {
		userRepository.deleteById(id);
	}
	
	
	public User updateEntire(User user, Integer id) 
	{
		Optional<User> dbUser = userRepository.findById(id);
		if (dbUser.isPresent()) {
			return userRepository.save(user);
		}
		return null;
	}

	
	
}
