package org.springmeetup.backend.backendspringboot.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticatedUser {

	private String username;
	private String clientName;
	private String token;

	private Set<String> rolesSet;
	private Set<String> realmRoleSet;
	private Set<String> resourceRoleSet;

}
