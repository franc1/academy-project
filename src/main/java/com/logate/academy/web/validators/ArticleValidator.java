package com.logate.academy.web.validators;

import java.util.Date;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.logate.academy.domains.Article;

@Description(value = "Article Validator.")
@Component
public class ArticleValidator implements Validator {
	

	@Override
	public boolean supports(Class<?> arg0) {
		return arg0.isAssignableFrom(Article.class);
	}

	@Override
	public void validate(Object target, Errors errors) 
	{
		Article article = (Article) target;
		
		validateTitle(article.getTitle(), errors);
		validateDate(article.getPublishedAt(), errors);
	}
	
	
	private void validateTitle(String title, Errors errors)
	{
		if (title == null)
		{
			errors.rejectValue("title", 
					"title.required", 
					"Field `title` is required.");
			return;
		}
		
		if (title.trim().contentEquals(""))
		{
			errors.rejectValue("title", 
					"title.empty", 
					"Field `title` is empty.");
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
