package org.springmeetup.backend.backendspringboot.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled =  true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final ApiSecurityContextRepository apiSecurityContextRepository;

	public static final String[] WHITE_LIST = {
			"/user/login",
			"/user/application/list",
			"/actuator/health",
			"/v2/api-docs",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-resources/**",
			"/swagger-ui.html*",
			"/webjars/**"
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.formLogin().disable()
		.httpBasic().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.securityContext().securityContextRepository(apiSecurityContextRepository)
		.and()
		.authorizeRequests()
		.antMatchers(WHITE_LIST).permitAll()
		.anyRequest().authenticated()
		.and()
		.exceptionHandling()
				.authenticationEntryPoint(new RestAuthenticationEntryPoint());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/",
				"/v2/api-docs",           // swagger
				"/webjars/**",            // swagger-ui webjars
				"/swagger-resources/**",  // swagger-ui resources
				"/configuration/**",      // swagger configuration
				"/*.html",
				"/favicon.ico",
				"/**/*.html",
				"/**/*.css",
				"/**/*.js");
	}

}
