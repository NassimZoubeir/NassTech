package com.ensup.nasstech.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ensup.nasstech.service.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
    private CustomUserDetailsService userDetailsService;
		
	 @Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	     http
	         .authorizeHttpRequests(authz -> authz
	             .requestMatchers("/profil").authenticated() // accessible à tout utilisateur connecté + admin pour le moment
	            //.requestMatchers("/admin","/creer-ordinateur").hasAuthority("administrateur") // accessible uniquement à un administrateur
	             .anyRequest().permitAll() // tout le reste est librement accessible
	         )
	         .formLogin(form -> form
	             .loginPage("/profil")
	             .loginProcessingUrl("/connexion-validation")
	             .defaultSuccessUrl("/accueil", true)
	             .permitAll()
	         )
	         .logout(logout -> logout
	             .logoutUrl("/deconnexion")
	             .logoutSuccessUrl("/accueil")
	         )
	         .userDetailsService(userDetailsService)
	         .csrf(AbstractHttpConfigurer::disable);

	     return http.build();
	 }

}

