package com.techsphere.techsphere.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private static final String[] WHITELIST = {
            "/",
            "/login",
            "/register",
            "/db-console/**",
            "/resources/**",
            "/posts/**",
            "/css/**",
            "/images/**",
            "/js/**",
            "/uploads/**"
    };

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
        .requestMatchers(WHITELIST).permitAll()
        .requestMatchers("/profile/**").authenticated()
        .requestMatchers("/update_photo/**").authenticated()
        .requestMatchers("/posts/add/**").authenticated()
        .requestMatchers("/comments/delete/**").authenticated()
        .requestMatchers("/", "/comments/add", "/{headingId}").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/editor/**").hasAnyRole("ADMIN", "EDITOR")
        )
        .formLogin(login -> login
        .loginPage("/login").loginProcessingUrl("/login")
        .usernameParameter("email").passwordParameter("password")
        .defaultSuccessUrl("/", true).failureUrl("/login?error")
        .permitAll()
        )
        .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .logoutSuccessUrl("/")
        )
        .rememberMe(rememberMe -> rememberMe
        .rememberMeParameter("remember-me")
        )
        .cors(cors -> cors.configurationSource(corsConfigurationSource())
        )
        .csrf(csrf -> csrf.disable()
        )
        .headers(headers -> headers
        .frameOptions(frame -> frame.sameOrigin())
        );

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration());
        return new CorsFilter(source);
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedOrigins(Arrays.asList("*")); 
        corsConfig.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); 
        return corsConfig;
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration());
        return source;
    }
}