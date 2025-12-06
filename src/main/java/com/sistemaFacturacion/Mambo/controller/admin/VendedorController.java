package com.sistemaFacturacion.Mambo.controller.admin;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sistemaFacturacion.Mambo.Service.VendedorService;
import com.sistemaFacturacion.Mambo.dto.VendedorDTO;
import com.sistemaFacturacion.Mambo.model.Usuario;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin/vendedores")
public class VendedorController {

    private final VendedorService vendedorService;

    public VendedorController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @GetMapping
    public String listaVendedores(Model model) {
        List<Usuario> vendedores = vendedorService.obtenerVendedores();
        model.addAttribute("vendedores", vendedores);
        model.addAttribute("vendedor", new VendedorDTO());
        return "admin/vendedores";
    }

    @PostMapping("guardar")
    public String guardarVendedor(@ModelAttribute VendedorDTO vendedor) {
        vendedorService.guardar(vendedor); 
        return "redirect:/admin/vendedores";
    }

    @PostMapping("eliminar/{id}")
    public String eliminarVendedor(@PathVariable Long id) {
        vendedorService.eliminar(id);
        return "redirect:/admin/vendedores";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public VendedorDTO obtenerVendedor(@PathVariable Long id) {
        VendedorDTO vendedor = vendedorService.buscarDTO(id);
        if (vendedor == null) {
            throw new RuntimeException("Vendedor no encontrado");
        }
        return vendedor;
    }
    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @GetMapping("/filtrar")
    @ResponseBody
    public List<VendedorDTO> filtrarVendedores(@RequestParam String buscar) {
        String busqueda = buscar.toLowerCase();

        return vendedorService.listar().stream()
                .filter(v -> 
                        (v.getId() != null && v.getId().toString().contains(busqueda)) ||
                        (v.getNombreCompleto() != null && v.getNombreCompleto().toLowerCase().contains(busqueda)) ||
                        (v.getEmail() != null && v.getEmail().toLowerCase().contains(busqueda))
                )
                .toList();
    }
}