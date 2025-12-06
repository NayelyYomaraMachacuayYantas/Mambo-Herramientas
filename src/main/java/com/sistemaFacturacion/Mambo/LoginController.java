package com.sistemaFacturacion.Mambo;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {


  @GetMapping({ "/login" })
  public String mostrarLogin() {
    return "login"; // muestra login.html
  }

}
