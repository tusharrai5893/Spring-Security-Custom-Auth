package com.tushar.supportportal.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tushar.supportportal.auth.userDetails.UserPrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.tushar.supportportal.constant.SecurityConstant.*;
import static java.util.Arrays.stream;

@Component
public class JwtTokenProvider {

	@Value("${JWT.SECRET}")
	public String SECRET;

	public String generateToken(UserPrincipalDetails upd) {
		String[] claims = getClaimsFromPrincipal(upd);
		return JWT.create()
				.withIssuer(REDDIT_READERS_LLC)
				.withAudience(REDDIT_READERS_LLC_ADMINISTRATION)
				.withIssuedAt(Instant.now())
				.withSubject(upd.getUsername())
				.withArrayClaim(AUTHORITIES, claims)
				.withExpiresAt(Instant.now().plusMillis(EXPIRATION_TIME))
				.sign(HMAC512(SECRET.getBytes())); // TODO check RSAKEY
	}

	public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, null, authorities);
		upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return upat;
	}

	public boolean isTokenValid(String token, String username) {
		return StringUtils.isNotEmpty(username) && !isTokenExpired(getVerifier(), token);
	}

	private boolean isTokenExpired(JWTVerifier verifier, String token) {
		return verifier.verify(token).getExpiresAtAsInstant().isBefore(Instant.now());
	}

	private String getSubjectFromToken(String token) {
		return getVerifier().verify(token).getSubject();
	}

	public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
		String[] claims = getClaimsFromToken(token);
		return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	private String[] getClaimsFromToken(String token) {
		JWTVerifier verifier = getVerifier();
		return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
	}

	private JWTVerifier getVerifier() {
		return Optional.of(JWT.require(HMAC512(SECRET)).withIssuer(REDDIT_READERS_LLC).build())
				.orElseThrow(() -> new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED));
	}

	private String[] getClaimsFromPrincipal(UserPrincipalDetails upd) {
		var authorities = new ArrayList<>(upd.getAuthorities());
		return authorities.toArray(new String[0]);
	}
}