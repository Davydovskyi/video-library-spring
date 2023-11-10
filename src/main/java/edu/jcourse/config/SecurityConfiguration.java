package edu.jcourse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .requestMatchers(
                                antMatcher("/login"), antMatcher("/registration"),
                                antMatcher(HttpMethod.POST, "/users")).permitAll()
                        .anyRequest().permitAll())
                .formLogin(login -> login.loginPage("/login")
                        .defaultSuccessUrl("/movies"))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }
}