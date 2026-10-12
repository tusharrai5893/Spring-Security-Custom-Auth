package com.tushar.supportportal.util;

import com.auth0.jwt.JWT;
import com.tushar.supportportal.auth.userDetails.UserPrincipalDetails;
import com.tushar.supportportal.util.interfaces.JwtTokenProvider;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.tushar.supportportal.constant.SecurityConstant.*;
import static java.util.Arrays.stream;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

	@Value("${JWT.SECRET}")
	public String SECRET;

	@Override
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

	@Override
	public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
		String[] claims = getClaimsFromToken(token);
		return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, null, authorities);
		upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return upat;
	}

	@Override
	public boolean isTokenValid(String token, String username) {
		return StringUtils.isNotEmpty(username) && !isTokenExpired(getVerifier(), token);
	}

	@Override
	public String getSubjectFromToken(String token) {
		return getVerifier().verify(token).getSubject();
	}
}