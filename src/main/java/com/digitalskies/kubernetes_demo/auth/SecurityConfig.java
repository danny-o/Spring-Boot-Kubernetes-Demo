package com.digitalskies.kubernetes_demo.auth;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private JWTAuthFilter jwtAuthFilter;

    public SecurityConfig(JWTAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    SecurityFilterChain configureSecurity(HttpSecurity httpSecurity){

        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(policy->policy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth->auth
                                .requestMatchers("/auth/**","/css/**")
                                .permitAll()
                                .dispatcherTypeMatchers(DispatcherType.ERROR,DispatcherType.FORWARD)
                                .permitAll()
                                .anyRequest()
                                .authenticated()

                        )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .failureUrl("/auth/login?error=true")
                        .loginProcessingUrl("/auth/xxxx")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
