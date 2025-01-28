package SecureKeeper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        

        // not working at browsers since `sessionManagment`, every request shold be sent with credentials
        return http
            // .csrf(customizer -> customizer.disable()) down bellow with method reference
            .csrf(AbstractHttpConfigurer::disable)
            // every request shold be authenticated
            .authorizeHttpRequests(request -> request.anyRequest().authenticated())
            // login form for browser
            .formLogin(Customizer.withDefaults())
            // login form for postman
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }
}
