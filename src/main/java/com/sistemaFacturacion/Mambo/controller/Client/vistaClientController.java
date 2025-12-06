package com.sistemaFacturacion.Mambo.controller.Client;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class vistaClientController {
    @GetMapping
    public String Inicio (@AuthenticationPrincipal UserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("nombreUsuario", user.getUsername()); // aquí podrías buscar el cliente/usuario completo
        }
        return "client/inicio"; 
        // --> ubica tu archivo en: src/main/resources/templates/client/inicio.html
    }
}
