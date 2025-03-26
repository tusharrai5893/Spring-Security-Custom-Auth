package com.tushar.supportportal.util;

import com.tushar.supportportal.auth.userDetails.UserPrincipalDetails;
import org.springframework.beans.factory.annotation.Value;

public class JwtTokenProvider {

	@Value("${JWT.SECRET}")
	public final String SECRET;

	public JwtTokenProvider(String secret) {
		this.SECRET = secret;
	}

	public String generateToken(UserPrincipalDetails upd) {
		return null;
	}
}