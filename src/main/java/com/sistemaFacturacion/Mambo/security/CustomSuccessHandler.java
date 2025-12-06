package com.sistemaFacturacion.Mambo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();
            System.out.println("Authority: " + role); // para depuración

            switch (role) {
                case "ROLE_ADMIN":
                    response.sendRedirect("/admin/home");
                    return;

                case "ROLE_VENDEDOR":
                    response.sendRedirect("/admin/productos");
                    return;

                case "ROLE_CLIENTE":
                    response.sendRedirect("/client/listadeproductos");
                    return;

                default:
                    break;
            }
        }

        // Si no coincide ningún rol
        response.sendRedirect("/login?error");
    }
}
