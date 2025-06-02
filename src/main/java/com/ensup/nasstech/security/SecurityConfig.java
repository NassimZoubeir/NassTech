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
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	   http
    	   .authorizeHttpRequests(authz -> authz
    	            .anyRequest().permitAll() // Autoriser toutes les requêtes sans restriction
    	        )
           .formLogin(form -> form
               .loginPage("/connexion")
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

