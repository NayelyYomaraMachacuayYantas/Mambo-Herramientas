package com.sistemaFacturacion.Mambo.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class vistaProductoController {

    @GetMapping("/listadeproductos")
    public String listarProductosCliente(Model model) {

        List<Map<String, Object>> productos = new ArrayList<>();

        productos.add(Map.of("id",1,"nombre","Polo","categoria","Poleras","precio",50.0,"stock",15,"imagenUrl","1760307102963_polo.jpg"));
        productos.add(Map.of("id",2,"nombre","Gorra","categoria","Accesorios","precio",30.0,"stock",20,"imagenUrl","1760616337420_gorra.jpg"));
        productos.add(Map.of("id",3,"nombre","Camisa","categoria","Poleras","precio",60.0,"stock",12,"imagenUrl","1760616359639_camisero.jpg"));
        productos.add(Map.of("id",4,"nombre","Camisa 2","categoria","Poleras","precio",65.0,"stock",10,"imagenUrl","1760616375134_camisero.jpg"));
        productos.add(Map.of("id",5,"nombre","Polo 2","categoria","Poleras","precio",55.0,"stock",18,"imagenUrl","1760616410561_polo2.jpg"));
        productos.add(Map.of("id",6,"nombre","Short","categoria","Shorts","precio",40.0,"stock",14,"imagenUrl","1760616513613_short1.jpg"));
        productos.add(Map.of("id",7,"nombre","Accesorio","categoria","Accesorios","precio",25.0,"stock",30,"imagenUrl","Accesorios.png"));
        productos.add(Map.of("id",8,"nombre","Chaqueta de Cuero","categoria","Chaquetas","precio",200.0,"stock",5,"imagenUrl","chaqueta_de_cuero.jpg"));
        productos.add(Map.of("id",9,"nombre","Chaqueta Denim","categoria","Chaquetas","precio",150.0,"stock",8,"imagenUrl","chaqueta_denim.jpg"));
        productos.add(Map.of("id",10,"nombre","Conjunto","categoria","Poleras","precio",120.0,"stock",6,"imagenUrl","conjunto.png"));
        productos.add(Map.of("id",11,"nombre","Gorra Blanca","categoria","Accesorios","precio",35.0,"stock",20,"imagenUrl","gorra_blanca.jpg"));
        productos.add(Map.of("id",12,"nombre","Llegadas","categoria","Ofertas","precio",100.0,"stock",10,"imagenUrl","llegadas.jpg"));
        productos.add(Map.of("id",13,"nombre","Mambo Logo","categoria","Ofertas","precio",80.0,"stock",15,"imagenUrl","mambo.jpeg"));
        productos.add(Map.of("id",14,"nombre","Ofertas","categoria","Ofertas","precio",70.0,"stock",12,"imagenUrl","ofertas.jpg"));
        productos.add(Map.of("id",15,"nombre","Pantalon","categoria","Pantalones","precio",90.0,"stock",20,"imagenUrl","pantalon.png"));
        productos.add(Map.of("id",16,"nombre","Pantalon Jean","categoria","Pantalones","precio",100.0,"stock",18,"imagenUrl","pantalon_jean.jpg"));
        productos.add(Map.of("id",17,"nombre","Pantalon Jogger","categoria","Pantalones","precio",95.0,"stock",16,"imagenUrl","pantalon_jogger.jpg"));
        productos.add(Map.of("id",18,"nombre","Polo Azul","categoria","Poleras","precio",50.0,"stock",15,"imagenUrl","polo_azul.jpg"));
        productos.add(Map.of("id",19,"nombre","Polo Negro","categoria","Poleras","precio",50.0,"stock",15,"imagenUrl","polo_negro.jpg"));
        productos.add(Map.of("id",20,"nombre","Polo Rojo","categoria","Poleras","precio",50.0,"stock",15,"imagenUrl","polo_rojo.jpg"));
        productos.add(Map.of("id",21,"nombre","Pulsera de Cuero","categoria","Accesorios","precio",40.0,"stock",25,"imagenUrl","pulsera_de_cuero.jpg"));
        productos.add(Map.of("id",22,"nombre","Short Deportivo","categoria","Shorts","precio",45.0,"stock",18,"imagenUrl","short_deportivo.jpg"));

        model.addAttribute("productos", productos);

        return "client/listadeproductos";
    }
}
