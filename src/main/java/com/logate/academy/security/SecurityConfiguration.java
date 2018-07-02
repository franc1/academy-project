package com.logate.academy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.logate.academy.security.components.UserDetailsComponent;
import com.logate.academy.security.exceptions.HttpAuthenticationPoint;
import com.logate.academy.security.jwt.JWTConfigurer;
import com.logate.academy.security.jwt.TokenProvider;

@Description(value = "Spring Security Configuration.")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	// prvo se Security ogranicenja razmatraju ovdje...
	//.. pa tek onda u samim klasama ili metodama (koje imaju @PreAuthorize anotaciju).
	//.. Tj ako je ovdje servis 'authenticated()' request nece stici do controllera !!! 
	
	@Autowired
	private HttpAuthenticationPoint authenticationProvider;
	
	
	@Autowired
	private UserDetailsComponent userDetailsComponent;
	
	@Autowired
    private TokenProvider tokenProvider;

    @Bean(name = "bcryptEncode")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception 
    {
	    	authBuilder
	            .userDetailsService(userDetailsComponent)
	            .passwordEncoder(passwordEncoder());
    }


	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http
			.exceptionHandling().authenticationEntryPoint(authenticationProvider)  //ukoliko request nije autentifikovan
			.and()                                                  //...aktivira se exception authnticationProvider!
			.csrf().disable()    //onemogucava unosenje skripta(npr neki JS kod) u input poljima
			.headers().frameOptions().disable()
			.and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)   // znaci nema sesija!!!
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/employees/validator").permitAll()
            .antMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/api/roles").hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/api/articles").hasRole("DEVELOPER")
            .antMatchers(HttpMethod.PUT, "/api/articles/**").hasAnyRole("DEVELOPER", "ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/articles/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/api/articles/**").hasRole("USER")
            .antMatchers("/api/categories/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/api/comments").authenticated()
            .antMatchers(HttpMethod.PUT, "/api/comments").authenticated()
            .antMatchers(HttpMethod.DELETE, "/api/comments1/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/api/comments").hasRole("ADMIN")
            .antMatchers("/api/users/**").hasAnyRole("ADMIN", "DEVELOPER")
            .antMatchers("/api/employees/**").permitAll()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/api/file/upload/article/**").permitAll()
            .antMatchers("/api/file/upload/encoded/**").permitAll()
            .antMatchers("/api/file/download/**").permitAll()
            .antMatchers("/api/comments-like/**").permitAll()
            .antMatchers("/api/comments-dislike/**").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .apply(securityConfigurerAdapter());    // ovo ustvari znaci - primijeni JWT token!!!
	}
	
	
	private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
