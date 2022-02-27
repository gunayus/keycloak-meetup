package org.springmeetup.backend.backendspringboot.security.model;

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

	private Set<String> realmRoleSet;
	private Set<String> resourceRoleSet;

}
