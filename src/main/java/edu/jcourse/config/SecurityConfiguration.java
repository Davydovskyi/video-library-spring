package edu.jcourse.config;

import edu.jcourse.database.entity.Role;
import edu.jcourse.dto.user.AdaptedUserDetails;
import edu.jcourse.dto.user.CustomUserDetails;
import edu.jcourse.dto.user.UserCreateEditDto;
import edu.jcourse.dto.user.UserReadDto;
import edu.jcourse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Set;

import static edu.jcourse.util.HttpPath.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AnonymousConfigurer::disable)
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .requestMatchers(
                                antMatcher(LOGIN), antMatcher(REGISTRATION),
                                antMatcher("/v3/api-docs/**"), antMatcher("/swagger-ui/**"),
                                antMatcher(HttpMethod.POST, USERS)).permitAll()
                        .requestMatchers(
                                antMatcher("/users/{\\d+}/delete"), antMatcher("/users/{\\d+}/update"),
                                antMatcher("/movie-persons/add/{\\d+}"), antMatcher("/movies/add"),
                                antMatcher(HttpMethod.POST, MOVIES), antMatcher("/movies/{\\d+}/pre-update"),
                                antMatcher("/movies/{\\d+}/delete"), antMatcher("/movies/{\\d+}/update"),
                                antMatcher("/persons/add"), antMatcher(HttpMethod.POST, PERSONS),
                                antMatcher("/persons/{\\d+}/delete"), antMatcher("/persons/{\\d+}/update")).hasAuthority(Role.ADMIN.getAuthority())
                        .anyRequest().authenticated())
                .formLogin(login -> login.loginPage(LOGIN)
                        .defaultSuccessUrl(MOVIES))
                .logout(logout -> logout.logoutUrl(LOGOUT)
                        .logoutSuccessUrl(LOGIN)
                        .deleteCookies("JSESSIONID"))
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage(LOGIN)
                        .defaultSuccessUrl(MOVIES)
                        .userInfoEndpoint(userInfo ->
                                userInfo.oidcUserService(oidcUserService())));
        return http.build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return request -> {
            String email = request.getIdToken().getEmail();
            CustomUserDetails<Long> userDetails;
            try {
                userDetails = userService.loadUserByUsername(email);
            } catch (UsernameNotFoundException e) {
                UserCreateEditDto createEditDto = createUser(request.getIdToken());
                UserReadDto user = userService.create(createEditDto);
                userDetails = new AdaptedUserDetails(user.id(), user.email(),
                        passwordEncoder.encode(createEditDto.rawPassword()), Collections.singleton(user.role()));
            }

            CustomUserDetails<Long> finalUserDetails = userDetails;

            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), request.getIdToken());

            Set<Method> userDetailsMethods = Set.of(CustomUserDetails.class.getMethods());

            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[]{OidcUser.class, CustomUserDetails.class},
                    (proxy, method, args) ->
                            userDetailsMethods.contains(method)
                                    ? method.invoke(finalUserDetails, args)
                                    : method.invoke(oidcUser, args));
        };
    }

    private UserCreateEditDto createUser(OidcIdToken idToken) {
        return UserCreateEditDto.builder()
                .email(idToken.getEmail())
                .userName(idToken.getFullName())
                .role(Role.USER)
                .rawPassword(idToken.getAccessTokenHash())
                .build();
    }
}