package org.springmeetup.backend.backendspringboot.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiSecurityContextRepository implements SecurityContextRepository {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String ACTUATOR_HEALTH_REQUEST = "/actuator/health";

	private List<String> whiteListUrls = Arrays.asList(WebSecurityConfig.WHITE_LIST);

	private final JwtDecoder jwtDecoder;

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		String requestUri = requestResponseHolder.getRequest().getRequestURI();

		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

		try {
			HttpServletRequest httpServletRequest = requestResponseHolder.getRequest();

			AuthenticatedUser authenticatedUser = fetchAuthenticatedUser(httpServletRequest);

			if (authenticatedUser == null) {
				securityContext.setAuthentication(null);
			} else {
				List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();
				grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_API_USER"));
				if (authenticatedUser.getRolesSet() != null) {
					authenticatedUser.getRolesSet().forEach(roleName -> grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + roleName)));
				}

				Authentication authenticated = new UsernamePasswordAuthenticationToken(
						authenticatedUser,
						null,
						grantedAuthorityList
				);

				securityContext.setAuthentication(authenticated);
				MDC.put("username", authenticatedUser.getUsername());
			}
		} catch (Exception ex) {
			if (! whiteListUrls.contains(requestUri)) {
				log.debug("unauthorized request is received : {}", ex.getMessage());
			}
		}

		return securityContext;
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		String jwtToken = request.getHeader(AUTHORIZATION_HEADER);

		return jwtToken != null;
	}

	private AuthenticatedUser fetchAuthenticatedUser(HttpServletRequest request) {
		String keycloakJwtToken = request.getHeader(AUTHORIZATION_HEADER);

		if (keycloakJwtToken == null) {
			throw new UnauthorizedException("Token is missing");
		}

		return getAuthenticatedUserFromKeycLoakToken(keycloakJwtToken);
	}

	private AuthenticatedUser getAuthenticatedUserFromKeycLoakToken(String keycloakJwtToken) {
		keycloakJwtToken = keycloakJwtToken.replace("Bearer ", "");

		Jwt jwtDecoded;
		try {
			jwtDecoded = jwtDecoder.decode(keycloakJwtToken);
		} catch (Exception ex) {
			log.error("JWT token Exception: ",ex);
			throw new UnauthorizedException("JWT token is not valid");
		}

		String clientName = jwtDecoded.getClaimAsString("azp");

		Set<String> realmRoleSet = getRealmRoles(jwtDecoded);
		Set<String> clientResourceRoleSet = getResourceRoles(jwtDecoded, clientName);

		String username = jwtDecoded.getClaimAsString("preferred_username");
		return AuthenticatedUser.builder()
				.username(username)
				.clientName(clientName)
				.token(keycloakJwtToken)
				.realmRoleSet(realmRoleSet)
				.resourceRoleSet(clientResourceRoleSet)
				.build();
	}

	private Set<String> getRealmRoles(Jwt jwtDecoded) {
		Set<String> rolesSet = new LinkedHashSet<>();
		if (jwtDecoded.getClaimAsMap("realm_access") != null) {
			Map<String, Object> resourceAccessClaimsMap = jwtDecoded.getClaimAsMap("realm_access");
			if (!CollectionUtils.isEmpty(resourceAccessClaimsMap)) {
				if (resourceAccessClaimsMap.get("roles") != null) {
					rolesSet.addAll((List)resourceAccessClaimsMap.get("roles"));
				}
			}
		}
		return rolesSet;
	}

	private Set<String> getResourceRoles(Jwt jwtDecoded, String clientName) {
		Set<String> rolesSet = new LinkedHashSet<>();
		if (jwtDecoded.getClaimAsMap("resource_access") != null) {
			Map<String, Object> resourceAccessClaimsMap = jwtDecoded.getClaimAsMap("resource_access");
			if (!CollectionUtils.isEmpty(resourceAccessClaimsMap)) {
				Map<String, Object> clientResourceAcessClaimsMap = (Map) resourceAccessClaimsMap.get(clientName);
				if (clientResourceAcessClaimsMap.get("roles") != null) {
					rolesSet.addAll((List)clientResourceAcessClaimsMap.get("roles"));
				}
			}
		}
		return rolesSet;
	}

	@Override
	public void saveContext(SecurityContext arg0, HttpServletRequest arg1, HttpServletResponse arg2) {
	}

}
