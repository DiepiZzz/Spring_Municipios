package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Nuevo Import
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.Service.UsuarioService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // NO necesitas @Autowired aquí para UsuarioService
    // Lo vamos a pasar como argumento al método bean de authenticationProvider

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/login",
                "/register",
                "/forgot-password", // Permite acceso público a esta URL
                "/reset-password",  // Permite acceso público a esta URL
                "/css/**",
                "/js/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // <<-- ESTE ES EL CAMBIO CLAVE PARA ELIMINAR LA REFERENCIA CIRCULAR -->>
    // Configura el DaoAuthenticationProvider como un Bean
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService); // Usa tu UsuarioService
        authProvider.setPasswordEncoder(passwordEncoder); // Usa tu PasswordEncoder
        return authProvider;
    }

    // ELIMINA ESTE MÉTODO POR COMPLETO:
    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder());
    }
    */
}