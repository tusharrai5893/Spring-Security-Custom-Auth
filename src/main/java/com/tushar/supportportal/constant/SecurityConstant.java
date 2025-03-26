package com.tushar.supportportal.constant;

public class SecurityConstant {
	public static final long EXPIRATION_TIME =  5 * 24 * 3600; // 5 Days;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String X_JWT_TOKEN_HEADER = "X-Jwt-Token";
	public static final String TOKEN_CANNOT_BE_VERIFIED = "TOKEN CANNOT BE VERIFIED";
	public static final String ORGANISATION_NAME = "REDDIT READERS LLC";
	public static final String ORGANISATION_ADMIN = "User Management Portal";
	public static final String AUTHORITIES = "authorities";
	public static final String FORBIDDEN = "You do not have permission to access this resource";
	public static final String ACCESS_DENIED_UNAUTHORIZED = "You do not have permission to access this resource";
	public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
	public static final String[] PUBLIC_URLS =
			{"/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/reset-password", "/api/v1/user/image/**"};
}