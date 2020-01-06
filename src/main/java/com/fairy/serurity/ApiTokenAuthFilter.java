package com.fairy.serurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class ApiTokenAuthFilter extends AbstractAuthenticationProcessingFilter {

	public static final String AUTHORIZATION_HEADER_NAME = "authorization";
	public static final String USERNAME = "username";

	public ApiTokenAuthFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String accessToken = request.getHeader(AUTHORIZATION_HEADER_NAME);
		String username = request.getHeader(USERNAME);
		if (accessToken != null && username != null) { 
			return new PreAuthenticatedAuthenticationToken(username, accessToken);
		} else {
			throw new BadCredentialsException(username + " access deney");
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();

	}

}
