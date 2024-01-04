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
                .requestMatchers("/css/**", "/js/**", "/image/**")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/", "/login-success", "/logout-success", "/error")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/users/**")
                .hasAnyAuthority("ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/profile/**")
                .hasAnyAuthority("ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**")
                .hasAuthority("ADMIN")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/task/**", "api/**")
                .hasAnyAuthority("ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/upload/**")
                .hasAnyAuthority("ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/designer/**")
                .hasAnyAuthority("ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/message/**")
                .hasAnyAuthority("ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/work/**")
                .hasAnyAuthority("ADMIN", "OPERATOR", "DESIGNER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/edit/**")
                .hasAnyAuthority("ADMIN", "OPERATOR")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/work-manage/**")
                .hasAnyAuthority("ADMIN", "OPERATOR")
                .and()
                .formLogin()
                .defaultSuccessUrl("/login-success")
                .and()
                .logout()
                .logoutSuccessUrl("/logout-success")
                .permitAll()
                .and()
                .build();
    }
}
