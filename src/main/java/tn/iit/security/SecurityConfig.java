package tn.iit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth

                        // 🔓 Authentification publique
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/utils/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // ✅ EMPLOYÉS : Consultation accessible à tous les utilisateurs authentifiés
                        .requestMatchers(HttpMethod.GET, "/api/employes/**").authenticated()

                        // 🔐 ADMIN uniquement : Création, modification, suppression des employés
                        .requestMatchers(HttpMethod.POST, "/api/employes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/employes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/employes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/employes/**").hasRole("ADMIN")

                        // 🔐 DÉPARTEMENTS : Consultation accessible à tous les utilisateurs authentifiés
                        .requestMatchers(HttpMethod.GET, "/api/departements/**").authenticated()
                        // Modification des départements réservée à ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/departements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/departements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/departements/**").hasRole("ADMIN")

                        // 🔐 ADMIN + RESPONSABLE : opérations bureau d'ordre
                        .requestMatchers("/api/responsable/**")
                        .hasAnyRole("ADMIN", "RESPONSABLE_BUREAU")

                        // 🔐 AGENT + RESPONSABLE + ADMIN : ajout et traitement courrier
                        .requestMatchers("/api/agent/**")
                        .hasAnyRole("ADMIN", "RESPONSABLE_BUREAU", "AGENT")

                        // 🔐 COURRIER : Accès selon les rôles
                        .requestMatchers(HttpMethod.GET, "/api/courriers/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/courriers/**")
                        .hasAnyRole("ADMIN", "RESPONSABLE_BUREAU", "AGENT")
                        .requestMatchers(HttpMethod.PUT, "/api/courriers/**")
                        .hasAnyRole("ADMIN", "RESPONSABLE_BUREAU")
                        .requestMatchers(HttpMethod.DELETE, "/api/courriers/**").hasRole("ADMIN")

                        // 🔐 STATISTIQUES : Accessibles selon le rôle
                        .requestMatchers("/api/statistiques/**")
                        .hasAnyRole("ADMIN", "RESPONSABLE_BUREAU")

                        // 🔐 PROFIL UTILISATEUR : Accessible à tous les utilisateurs authentifiés
                        .requestMatchers("/api/auth/profile").authenticated()
                        .requestMatchers("/api/auth/me").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/pieces-jointes/download/**").authenticated() // utilisateur connecté
                        // Tout le reste doit être authentifié
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"success\":false,\"message\":\"Accès refusé ou identifiants incorrects\",\"data\":null}");
                }))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200", 
            "http://localhost:4201",
            "http://127.0.0.1:4200",
            "http://127.0.0.1:4201"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", 
            "Content-Type", 
            "Accept", 
            "Origin", 
            "X-Requested-With"
        ));
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Disposition"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}