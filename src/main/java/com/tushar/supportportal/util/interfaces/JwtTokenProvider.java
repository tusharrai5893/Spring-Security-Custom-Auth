package com.tushar.supportportal.util.interfaces;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tushar.supportportal.auth.userDetails.UserPrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.tushar.supportportal.constant.SecurityConstant.*;
import static io.undertow.server.protocol.ajp.AjpRequestParser.SECRET;

public interface JwtTokenProvider {

	String generateToken(UserPrincipalDetails upd);

	List<GrantedAuthority> getAuthoritiesFromToken(String token);

	Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request);

	boolean isTokenValid(String token, String username);

	String getSubjectFromToken(String token);

	default String[] getClaimsFromPrincipal(UserPrincipalDetails upd) {
		var authorities = new ArrayList<>(upd.getAuthorities());
		return authorities.toArray(new String[0]);
	}

	default boolean isTokenExpired(JWTVerifier verifier, String token) {
		return verifier.verify(token).getExpiresAtAsInstant().isBefore(Instant.now());
	}

	default String[] getClaimsFromToken(String token) {
		JWTVerifier verifier = getVerifier();
		return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
	}

	default JWTVerifier getVerifier() {
		return Optional.of(JWT.require(HMAC512(SECRET)).withIssuer(REDDIT_READERS_LLC).build())
				.orElseThrow(() -> new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED));
	}
}