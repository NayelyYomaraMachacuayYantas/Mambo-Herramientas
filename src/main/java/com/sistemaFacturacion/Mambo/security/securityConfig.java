package com.sistemaFacturacion.Mambo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class securityConfig {
        @Autowired
private JwtAuthenticationFilter jwtAuthFilter;

@Autowired
private AuthenticationProvider authenticationProvider;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 🔒 Deshabilitamos CSRF para desarrollo (puedes habilitarlo después si usas formularios seguros)
                .csrf(csrf -> csrf.disable())
                // 🔑 Configuramos permisos para las rutas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/img/**", "/admin/principal", "/api/auth/**", "/", "/cliente/**").permitAll()
                        .requestMatchers("/admin/reporte").hasRole("ADMIN")
                        .requestMatchers("/admin/boleta", "/admin/factura", "/admin/productos/**", "/lista/clientes/**",
                                "/admin/home", "lista/vendedores/**").hasAnyRole("VENDEDOR", "ADMIN")
                        .requestMatchers("/carrito/**", "/productos/**").hasRole("CLIENTE")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
              
                .formLogin(form -> form
                        .loginPage("/login")  // Ruta del login
                        .usernameParameter("username") // DNI o usuario
                        .passwordParameter("password") // Contraseña
                        .successHandler(customSuccessHandler) // Redirección según rol
                        .permitAll()
                )

                // 🚪 Configuración del logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }



}
