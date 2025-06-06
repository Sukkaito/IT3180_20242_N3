package vn.edu.hust.nmcnpm_20242_n3.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.request.WebRequest;
import vn.edu.hust.nmcnpm_20242_n3.service.UserService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                    .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                    .securityMatcher("/api/**") // Apply security to /api/** endpoints
                    .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with default settings
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**").permitAll() // Allow public access to login endpoint
                            .anyRequest().authenticated() // Require authentication for all other requests
                    )
                    .userDetailsService(userService) // Set custom UserDetailsService
                    .formLogin(AbstractHttpConfigurer::disable) // Disable form login for now
                    .httpBasic(Customizer.withDefaults()); // Enable basic authentication

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Error configuring security filter chain", e);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public ErrorAttributes errorAttributes() {
//        return new DefaultErrorAttributes() {
//            @Override
//            public Map<String, Object> getErrorAttributes(WebRequest webRequest,
//                                                          org.springframework.boot.web.error.ErrorAttributeOptions options) {
//                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
//                // Remove the stack trace
//                errorAttributes.remove("trace");
//                // Customize the error message
//                errorAttributes.put("message", "Access denied");
//                return errorAttributes;
//            }
//        };
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Adjust with your frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
