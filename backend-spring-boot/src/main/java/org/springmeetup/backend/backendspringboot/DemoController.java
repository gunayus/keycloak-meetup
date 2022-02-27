package org.springmeetup.backend.backendspringboot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springmeetup.backend.backendspringboot.security.model.AuthenticatedUser;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/demo")
@RequiredArgsConstructor
@Slf4j
public class DemoController {

	private final JwtDecoder jwtDecoder;

	@PostConstruct
	public void init() {
		log.info("callind post construct");
	}

	@GetMapping(value = "/auth-token-info")
	public Map<String, Object> getTokenInfo(Authentication authentication) {
		AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("auth-user", authenticatedUser);

		return responseBody;
	}

	@GetMapping(value = "/do-super-things")
	@PreAuthorize("hasRole('ROLE_SUPER_USER')")
	public String doSuperThings() {
		return "OK - super user operation is allowed";
	}

	@GetMapping(value = "/do-normal-things")
	@PreAuthorize("hasRole('ROLE_NORMAL_USER')")
	public String doNormalThings() {
		return "OK - normal user operation is allowed";
	}

}
