package tn.iit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Autoriser Angular sur localhost:4200
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        
        // Autoriser toutes les headers
        config.addAllowedHeader("*");
        
        // Autoriser toutes les méthodes HTTP
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS"); // IMPORTANT pour les preflight requests
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}