package com.logate.academy.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Description;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.ExpiredJwtException;


@Description(value = "JSON Web Token Filter.")
public class JWTFilter extends GenericFilterBean {
	
	private TokenProvider tokenProvider;
	
	public JWTFilter(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException 
	{
		// allow cross-origin requests... (da bi sa drugih domena mogli pristupiti ovim serivisima)!
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		((HttpServletResponse) response).setHeader("Access-Control-Max-Age", "86400"); // 24 Hours
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, x-auth-token, Access-Control-Allow-Origin");
		
		try
        {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String jwt = resolveToken(httpServletRequest);
            if (StringUtils.hasText(jwt))
            {
                if (tokenProvider.validateToken(jwt))
                {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException e) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
	}

	/**
	 * Method for resolving token from request
	 * 
	 * @param request - provided HTTP request
	 * @return token string
	 */
	private String resolveToken(HttpServletRequest request)
    {
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
