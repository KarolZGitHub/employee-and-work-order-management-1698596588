package com.employee.employeeandworkordermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors()
                .and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/register/**")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/css/**", "/js/**")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/users/**")
                .hasAnyAuthority("USER", "ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/profile/**")
                .hasAnyAuthority("USER", "ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**")
                .hasAuthority("ADMIN")
                .and()
                .formLogin().and().build();
    }
}
