package com.tushar.supportportal.auth.authenticationToken;

import com.tushar.supportportal.domain.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SupportPortalAuthenticationToken extends AbstractAuthenticationToken {

	private final User user;

	public SupportPortalAuthenticationToken(User user) {
		super(null);
		this.user = user;
		setAuthenticated(false);
	}

	public SupportPortalAuthenticationToken(User user, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.user = user;
		setAuthenticated(true);
	}

	public static SupportPortalAuthenticationToken unauthenticated(User user) {
		return new SupportPortalAuthenticationToken(user);
	}

	public static SupportPortalAuthenticationToken authenticated(User user) {
		return new SupportPortalAuthenticationToken(user, getGrantedAuthorities(user));
	}

	@Override
	public Object getCredentials() {
		return "[PASSWORD_PROTECTED]"; // No direct password, as JWT is used
	}

	@Override
	public Object getPrincipal() {
		return this.user;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return getGrantedAuthorities(this.user);
	}

	@Override
	public String getName() {
		return this.user.getUsername();
	}

	private static List<GrantedAuthority> getGrantedAuthorities(User user) {
		return Arrays.stream(user.getAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
}