package tn.iit.security;

import java.util.Arrays;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final CustomUserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
				).authorizeHttpRequests(auth -> auth

						
						.requestMatchers("/api/auth/**").permitAll().requestMatchers("/error").permitAll()

						// ADMIN — gestion des employés et départements
						.requestMatchers(HttpMethod.POST,   "/api/employes/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT,    "/api/employes/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/employes/**").hasRole("ADMIN")

						// RESPONSABLE peut lire les employés (pour envoyer un courrier)
						.requestMatchers(HttpMethod.GET, "/api/employes/**").hasAnyRole("ADMIN", "RESPONSABLE")

						// Départements — ADMIN seulement
						.requestMatchers("/api/departements/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PATCH, "/api/employes/*/password").hasRole("ADMIN")

						// ✅ COURRIERS
						// COURRIERS ENTRANTS (gérés uniquement par RESPONSABLE)
						.requestMatchers("/api/courriers-entrants/**").hasRole("RESPONSABLE")

						// COURRIERS SORTANTS (gérés uniquement par RESPONSABLE)
						.requestMatchers("/api/courriers-sortants/**").hasRole("RESPONSABLE")

						// ✅ tout le reste → authentifié
						.anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(401);
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write("{\"success\":false,\"message\":\"Non authentifié\",\"data\":null}");
				})).authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder()); // ✅ appel direct
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // ✅ défini ici, plus besoin de l'injecter
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:4201",
				"http://127.0.0.1:4200", "http://127.0.0.1:4201"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(
				Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
		configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}