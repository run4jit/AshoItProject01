package com.example.demo.Configurations;

import com.example.demo.Filters.TokenValidationFilter;
import com.example.demo.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TokenValidationFilter filter;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider(studentService);
        daoAuthProvider.setPasswordEncoder(getPasswordEncoder());

        return daoAuthProvider;
    }

    @Bean
    public AuthenticationManager getAuthManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req ->
            req.requestMatchers("/api/login", "/api/register").permitAll()
            .requestMatchers("/api/**").authenticated()
//            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            )
                .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                .authenticationProvider(getAuthenticationProvider())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return  http.csrf(csrf -> csrf.disable())
                .build();
    }
}
