package kh.edu.cstad.modilebankingaba.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class KeycloakSecurityConfig {

    /**
     * Configures the JWT converter to extract roles from Keycloak's 'realm_access' claim.
     * This converter maps the roles to Spring Security's `GrantedAuthority` objects.
     * @return The configured JwtAuthenticationConverter bean.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        Converter<Jwt, Collection<GrantedAuthority>> converter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
            Collection<String> authorities = realmAccess.get("roles");
            return authorities.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        };

        JwtAuthenticationConverter jwtGrantedAuthoritiesConverter = new JwtAuthenticationConverter();
        jwtGrantedAuthoritiesConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtGrantedAuthoritiesConverter;
    }

    /**
     * Configures the security filter chain for the application's REST API.
     * This method defines authorization rules, session management, and OAuth2 configuration.
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection for stateless APIs
                .csrf(csrf -> csrf.disable())

                // Configure authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Allow GET requests to /media/** without authentication (for file downloads)
                        .requestMatchers(HttpMethod.GET, "/media/**").permitAll()
                        // Allow POST to /customers/** only for ADMIN or STAFF roles
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers/**").hasAnyRole("ADMIN", "STAFF")
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )

                // Configure OAuth2 Resource Server for JWT token validation
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))

                // Disable form-based login
                .formLogin(form -> form.disable())

                // Set session management to stateless, as JWTs are self-contained
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
