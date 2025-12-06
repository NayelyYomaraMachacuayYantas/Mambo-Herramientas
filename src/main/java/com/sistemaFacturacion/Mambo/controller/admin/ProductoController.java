package com.sistemaFacturacion.Mambo.controller.admin;

import com.sistemaFacturacion.Mambo.Service.CategoriaService;
import com.sistemaFacturacion.Mambo.Service.ProductoService;
import com.sistemaFacturacion.Mambo.dto.ProductoDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    // 🔹 Inyección por constructor
    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    // Lista de producto
    @GetMapping
    public String listarProductos(Model model) {
        var producto = productoService.findAll();
        var categoria = categoriaService.listarCategorias();
        
        //contar la cantidad de productos
        int totalProductos = producto.size();
        //contar la cantidad de categoria
        int totalCategorias = categoria.size();

        //Cantidad de dinero en almacen
        double totalDinero = producto.stream()
                .mapToDouble(p -> p.getPrecio() * p.getStock())
                .sum();

        model.addAttribute("totalDinero", totalDinero);
        model.addAttribute("totalCategoria", totalCategorias);
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("productoDTO", new ProductoDTO()); 
        return "admin/productos"; 
    }

    // crear producto
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute ProductoDTO productoDTO) {
        productoService.save(productoDTO);
        return "redirect:/admin/productos";
    }

    // Actualizar producto
    @PostMapping("/actualizar")
    public String actualizarProducto(@ModelAttribute ProductoDTO productoDTO) {
        productoService.actualizar(productoDTO.getId(), productoDTO);
        return "redirect:/admin/productos";
    }


    // ✅ Eliminar producto
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.deleteById(id);
        return "redirect:/admin/productos";
    }

}
