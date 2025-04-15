package com.example.recipebook.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.example.recipebook.security.ApiAuthenticationFilter;
import com.example.recipebook.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	@Lazy
	private ApiAuthenticationFilter apiAuthenticationFilter;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
//	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.cors();
        http.authorizeHttpRequests().anyRequest().permitAll();
//		http.authorizeHttpRequests(request ->
//		request.requestMatchers("/recipebook/register", "/recipebook/login").permitAll()
//		.requestMatchers("/admin/recipebook/login").permitAll()
//		.requestMatchers("/admin/recipebook/**").hasAuthority("ROLE_ADMIN")
//				.anyRequest().authenticated());
//
//		http.formLogin(form -> form
//				.loginPage("/admin/recipebook/login")
//				.defaultSuccessUrl("/admin/recipebook/dashboard", true)
//				.failureUrl("/admin/recipebook/login?error=true"))
//		.logout(form -> form
//				.invalidateHttpSession(true).clearAuthentication(true)
//				.logoutRequestMatcher(new AntPathRequestMatcher("/admin/recipebook/logout"))
//				.logoutSuccessUrl("/login").permitAll()
//				);
//		http.httpBasic().disable();
//		http.addFilterBefore(apiAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

}
