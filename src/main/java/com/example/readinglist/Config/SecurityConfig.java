package com.example.readinglist.Config;


import com.example.readinglist.Repository.ReaderRepository;
import com.example.readinglist.Repository.ReadingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private ReaderRepository readerRepository;

    public SecurityConfig(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }
    //this a clean and modren way, instead of customeUserDeatailsService class
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> readerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in system: " + username));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").hasRole("READER"))
                .formLogin(form-> form.permitAll());

        return http.build();
    }
    //Pass your own userDetailsService() bean directly as a method call here, not like obj
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Link your service
        authProvider.setPasswordEncoder(passwordEncoder());     // Link your encoder
        return authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
