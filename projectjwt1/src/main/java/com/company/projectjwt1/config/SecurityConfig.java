package com.company.projectjwt1.config;

import com.company.projectjwt1.JWTFilter;
import com.company.projectjwt1.entity.Client;
import com.company.projectjwt1.repo.ClientRepository;
import com.company.projectjwt1.security.MyCustomEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /**httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/register", "/api/login").permitAll()
                        .requestMatchers("api/clients").authenticated()
                        .anyRequest().authenticated()
                )
//                .httpBasic(Customizer.withDefaults())
                // Customizing httpBasic to give customize Error message when user try to access private resource without authentication 1st
                .httpBasic(httpBasic -> httpBasic
                        .realmName("MyCustomRealm")
                        .authenticationEntryPoint(new MyCustomEntryPoint())) //For HTTP Basic's errors
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); */
        logger.warn("************** This is SecurityConfig's securityFilterChain method call.....");
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->authorize
                        .requestMatchers("/api/register", "/api/login").permitAll()
                        .requestMatchers("api/clients").authenticated()
                        .anyRequest().authenticated()
                )

                .exceptionHandling( exception -> exception
                        .authenticationEntryPoint(new MyCustomEntryPoint()) //handles JWT errors
                )
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        logger.warn("************** HttpSecurity ;;; "+httpSecurity);
        return httpSecurity.build();
    }

    /**
     * Without creating of classes we override loadByUsername in UserDetailsService using lambda expression,
     * and internally spring security will use DaoAuthenticationProvider and uses UserDetailsService
     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            Optional<Client> opt = clientRepo.findByEmailId(username);
//            if (opt.isEmpty())
//                throw new UsernameNotFoundException("User not found");
//            return User.builder()
//                    .username(opt.get().getEmailId())
//                    .password(opt.get().getPassword())
//                    .roles(opt.get().getRole().toUpperCase())
//                    .build();
//        };
//    }

    /**
     * This password encode bean will gets used by spring security automatically to compare both user's entered password
     * and password available in database of that particular username
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("**************This is SecutityConfig class calls and passwordEncoder method Execution");
        return new BCryptPasswordEncoder(12);
    }

    /**
     * this AuthenticationManager bean is to use for JWTFilter
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        logger.info("**************This is SecurityConfig class calls and authenticationManager method Execution");
        return configuration.getAuthenticationManager();
    }
}
