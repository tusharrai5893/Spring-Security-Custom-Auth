package com.tushar.supportportal.auth.filters;

import com.tushar.supportportal.util.interfaces.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.tushar.supportportal.constant.SecurityConstant.BEARER_TOKEN_PREFIX;
import static com.tushar.supportportal.constant.SecurityConstant.OPTIONS_HTTP_METHOD;

@Component
public class JwtOPRFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	public JwtOPRFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || !header.startsWith(BEARER_TOKEN_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = header.substring(BEARER_TOKEN_PREFIX.length());
		String username = jwtTokenProvider.getSubjectFromToken(token);
		if (jwtTokenProvider.isTokenValid(token, username) && SecurityContextHolder.getContext().getAuthentication() == null) {
			List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(token);
			Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}
}