package SecureKeeper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import SecureKeeper.service.OwnUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // UserDetailsService
    @Autowired
    private OwnUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        // not working at browsers since `sessionManagment`, every request shold be sent with credentials
        return http
            // .csrf(customizer -> customizer.disable()) down bellow with method reference
            .csrf(AbstractHttpConfigurer::disable)
            // every request shold be authenticated except register and login page
            .authorizeHttpRequests(request -> request
                .requestMatchers("register", "login")
                .permitAll()
                .anyRequest().authenticated())
            // login form for postman
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    // rewriting user details service
    // hardcoded values
    // @Bean
    // public UserDetailsService userDetailsService() {

    //     UserDetails user1 = User
    //         .withDefaultPasswordEncoder()
    //         .username("root")
    //         .password("root")
    //         .roles("USER")
    //         .build();

    //     return new InMemoryUserDetailsManager(user1);
    // }

    // taking data from db
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // decrypting password
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


    // overriding login logic
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
