package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> requests
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("PRODAVAC")
                .requestMatchers(new AntPathRequestMatcher("/users/**")).hasAnyRole("KUPAC", "PRODAVAC")
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                .anyRequest().authenticated())
		.formLogin(form -> form
        .loginPage("/pages/login.jsp").permitAll()
        .loginProcessingUrl("/login") //da li ovde treba index umesto login?
        .defaultSuccessUrl("/users/store"))
		.exceptionHandling(handling -> handling.accessDeniedPage("/pages/deniedAccess.jsp"))
		.csrf(csrf -> csrf.disable());

		return http.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}
	
//	@Bean
//	UserDetailsService userDetailsService() {
//		UserDetails userDetails = User.withUsername("admin")
//			.password(passwordEncoder().encode("123456"))
//			.roles("PRODAVAC")
//			.build();
//
//		return new InMemoryUserDetailsManager(userDetails);
//	}
	
	@Bean
	UserDetailsService customUserDetailsService() {
		return new CustomKorisnikService();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

