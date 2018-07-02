package com.logate.academy.web.validators;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.logate.academy.domains.Employee;
import com.logate.academy.domains.User;
import com.logate.academy.repository.UserRepository;
import com.logate.academy.web.dto.EmployeeDTO;

@Description(value = "User Validator.")
@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return arg0.isAssignableFrom(User.class);
	}

	@Override
	public void validate(Object target, Errors errors) 
	{
		User user = (User) target;
		
		validateFirstName(user.getFirstName(), errors);
		validateLastName(user.getLastName(), errors);
		validateUsername(user.getUsername(), errors);
	}
	
	
	private void validateFirstName(String firstName, Errors errors)
	{
		if (firstName == null)
		{
			errors.rejectValue("firstName", 
					"firstName.required", 
					"Field `firstName` is required.");
			return;
		}
		
		if (firstName.trim().contentEquals(""))
		{
			errors.rejectValue("firstName", 
					"firstName.empty", 
					"Field `firstName` is empty.");
		}
	}
	
	private void validateLastName(String lastName, Errors errors)
	{
		if (lastName == null)
		{
			errors.rejectValue("lastName", 
					"lastName.required", 
					"Field `lastName` is required.");
			return;
		}
		
		if (lastName.trim().contentEquals(""))
		{
			errors.rejectValue("lastName", 
					"lastName.empty", 
					"Field `lastName` is empty.");
		}
	}
	
	
	private void validateUsername(String username, Errors errors)
	{
		if (username == null)
		{
			errors.rejectValue("username", 
					"username.required", 
					"Field `username` is required.");
			return;
		}
		
		if (username.trim().contentEquals(""))
		{
			errors.rejectValue("username", 
					"username.empty", 
					"Field `username` is empty.");
			return;
		}
		
		if (username.trim().length()<4)
		{
			errors.rejectValue("username", 
					"username.short", 
					"Field `username` - minimum length is 4 charachters.");
		}
	}
	
}
