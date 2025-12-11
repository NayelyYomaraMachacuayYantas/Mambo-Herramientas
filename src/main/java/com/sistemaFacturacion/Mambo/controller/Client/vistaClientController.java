package com.sistemaFacturacion.Mambo.controller.Client;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")   // BASE: todas las URLs de este controlador empiezan en "/"
public class vistaClientController {

    @GetMapping
    public String Inicio(@AuthenticationPrincipal UserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("nombreUsuario", user.getUsername());
        }
        return "client/inicio"; 
        // templates/client/inicio.html
    }

    @GetMapping("/sobre-nosotros")
    public String sobreNosotros(@AuthenticationPrincipal UserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("nombreUsuario", user.getUsername());
        }
        return "client/sobre-nosotros";  // templates/client/sobre-nosotros.html
    }
}
