package ksiazkopol.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(this.passwordEncoder().encode("admin"))
                .roles("LIBRARIAN", "READER", "ADMIN")
                .build();

        UserDetails librarian = User.withUsername("librarian")
                .password(this.passwordEncoder().encode("1234"))
                .roles("LIBRARIAN")
                .build();

        UserDetails reader = User.withUsername("reader")
                .password(this.passwordEncoder().encode("reader"))
                .roles("READER")
                .build();

        UserDetails analyst = User.withUsername("analyst")
                .password(this.passwordEncoder().encode("1111"))
                .roles("ANALYST")
                .build();

        return new InMemoryUserDetailsManager(admin, librarian, reader, analyst);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry
                        .requestMatchers(HttpMethod.GET, "/api/books").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/books/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/book-series").permitAll()
                        .requestMatchers("/api/books/**").authenticated()
                        .requestMatchers("/api/readers/**").authenticated()
                        .requestMatchers("/api/book-series/**").authenticated()
                        .requestMatchers("/api/status/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)  // for POST requests via Postman
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
