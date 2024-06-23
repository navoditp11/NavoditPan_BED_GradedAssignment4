

package com.greatlearning.emwa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.greatlearning.emwa.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/h2-console/").permitAll()
                .requestMatchers("/api/user", "/api/role").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/employees").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/employees").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/*").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasicCustomizer -> {})
            .cors(corsCustomizer -> corsCustomizer.disable())
            .csrf(csrfCustomizer -> csrfCustomizer.disable());

        return http.build();
    }
}
