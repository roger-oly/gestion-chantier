package com.gestionchantier.backend.config;


import com.gestionchantier.backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

   .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/chantiers/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER",
        "OUVRIER"
    )

.requestMatchers(HttpMethod.POST, "/api/chantiers/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET"
    )

.requestMatchers(HttpMethod.PUT, "/api/chantiers/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET"
    )

.requestMatchers(HttpMethod.DELETE, "/api/chantiers/**")
    .hasRole("ADMINISTRATEUR")


    .requestMatchers(HttpMethod.GET, "/api/avancements/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER",
        "OUVRIER"
    )

.requestMatchers(HttpMethod.POST, "/api/avancements/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER"
    )

.requestMatchers(HttpMethod.DELETE, "/api/avancements/**")
.hasRole("ADMINISTRATEUR")

.requestMatchers(HttpMethod.GET, "/api/taches/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER",
        "OUVRIER"
    )
    .requestMatchers(HttpMethod.POST, "/api/taches/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER"
    )
    .requestMatchers(HttpMethod.PUT, "/api/taches/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER"
    )
.requestMatchers(HttpMethod.DELETE, "/api/taches/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER"
    )

.requestMatchers(HttpMethod.GET, "/api/incidents/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER",
        "OUVRIER"
    )

.requestMatchers(HttpMethod.POST, "/api/incidents/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER",
        "OUVRIER"
    )

.requestMatchers(HttpMethod.PUT, "/api/incidents/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER"
    )

    .requestMatchers(HttpMethod.DELETE, "/api/incidents/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION"
    )
.requestMatchers(HttpMethod.GET, "/api/livraisons/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER",
        "OUVRIER"
    )

.requestMatchers(HttpMethod.POST, "/api/livraisons/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER",
        "FOURNISSEUR"
    )

.requestMatchers(HttpMethod.PUT, "/api/livraisons/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER"
    )

.requestMatchers(HttpMethod.DELETE, "/api/livraisons/**")
    .hasAnyRole("ADMINISTRATEUR",
         "DIRECTION"
    )

    .requestMatchers(HttpMethod.GET, "/api/documents/**")
    .hasAnyRole(
    "ADMINISTRATEUR",
    "DIRECTION",
    "RESPONSABLE_PROJET",
    "CHEF_CHANTIER",
    "OUVRIER"
)

.requestMatchers(HttpMethod.POST, "/api/documents/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER"
    )

.requestMatchers(HttpMethod.PUT, "/api/documents/**")
    .hasAnyRole(
        "ADMINISTRATEUR",
        "DIRECTION",
        "RESPONSABLE_PROJET",
        "CHEF_CHANTIER"
    )

.requestMatchers(HttpMethod.DELETE, "/api/documents/**")
    .hasAnyRole("ADMINISTRATEUR",
         "DIRECTION"
    )

    .requestMatchers(HttpMethod.GET, "/api/utilisateurs/**")
    .hasRole("ADMINISTRATEUR")

.requestMatchers(HttpMethod.POST, "/api/utilisateurs/**")
    .hasRole("ADMINISTRATEUR")


      .requestMatchers(HttpMethod.PUT, "/api/utilisateurs/{id}/mot-de-passe")
    .authenticated()

.requestMatchers(HttpMethod.PUT, "/api/utilisateurs/**")
    .hasRole("ADMINISTRATEUR")

.requestMatchers(HttpMethod.DELETE, "/api/utilisateurs/**")
    .hasRole("ADMINISTRATEUR")

  

    .requestMatchers(HttpMethod.GET, "/api/notifications/utilisateur/**")
    .authenticated()

.requestMatchers(HttpMethod.PUT, "/api/notifications/*/lue")
    .authenticated()

.requestMatchers(HttpMethod.DELETE, "/api/notifications/**")
    .authenticated()

.requestMatchers(HttpMethod.POST, "/api/notifications")
    .hasRole("ADMINISTRATEUR")

.anyRequest().authenticated()
)

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}