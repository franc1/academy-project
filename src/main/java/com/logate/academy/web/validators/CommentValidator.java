package com.logate.academy.web.validators;

import java.util.Date;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.logate.academy.domains.Comment;
import com.logate.academy.web.dto.CommentDTO;

@Description(value = "Comment Validator.")
@Component
public class CommentValidator implements Validator {
	

	@Override
	public boolean supports(Class<?> arg0) {
		return arg0.isAssignableFrom(CommentDTO.class);
	}

	@Override
	public void validate(Object target, Errors errors) 
	{
		CommentDTO commentDTO = (CommentDTO) target;
		
		validateBody(commentDTO.getBody(), errors);
		validateDate(commentDTO.getPublishedAt(), errors);
	}
	
	
	private void validateBody(String body, Errors errors)
	{
		if (body == null)
		{
			errors.rejectValue("body", 
					"body.required", 
					"Field `body` is required.");
			return;
		}
		
		if (body.trim().contentEquals(""))
		{
			errors.rejectValue("body", 
					"body.empty", 
					"Field `body` is empty.");
		}
	}
	
	private void validateDate(Date date, Errors errors)
	{
		Date now = new Date();
		if (date.after(now)){
			errors.rejectValue("date", 
					"publishedAt.incorrect", 
					"Field `publishedAt` is incorrect.");
		}
	}
	
	
}
