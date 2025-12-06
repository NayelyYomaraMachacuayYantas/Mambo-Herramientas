package com.sistemaFacturacion.Mambo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;

import com.sistemaFacturacion.Mambo.Service.ClienteService;
import com.sistemaFacturacion.Mambo.Service.TipoDocumentoService;
import com.sistemaFacturacion.Mambo.dto.ClienteDTO;

@Controller
@RequestMapping("/lista/clientes")
public class ListaClientesController {

    private final ClienteService clienteService;
    private final TipoDocumentoService tDocumentoService;

    public ListaClientesController(ClienteService clienteService, TipoDocumentoService tDocumentoService) {
        this.clienteService = clienteService;
        this.tDocumentoService = tDocumentoService;
    }

    // ✅ Listar clientes
    @GetMapping
    public String listaClientes(Model model) {
        var clientes = clienteService.listarClientes();

        model.addAttribute("clientes", clientes);
        model.addAttribute("cantClientes", clientes.size());
        // el template usa th:object="${cliente}" -> usar el mismo nombre
        model.addAttribute("cliente", new ClienteDTO());
        model.addAttribute("tiposDocumento", tDocumentoService.listarTodos());

        return "admin/clientes";
    }

    // ✅ Obtener cliente por ID (para editar)
    @GetMapping("/obtener/{id}")
    @ResponseBody
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Guardar cliente
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") ClienteDTO clienteDTO) {
        try {
            clienteService.crearCliente(clienteDTO);
            return "redirect:/lista/clientes?success";
        } catch (DataIntegrityViolationException dive) {
            // probable unique constraint (email o documento)
            return "redirect:/lista/clientes?error=dup";
        } catch (Exception e) {
            return "redirect:/lista/clientes?error";
        }
    }

    // ✅ Actualizar cliente
    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable Long id, @ModelAttribute("cliente") ClienteDTO clienteDTO) {
        clienteDTO.setId(id);
        clienteService.actualizarCliente(id, clienteDTO);
        return "redirect:/lista/clientes?updated";
    }

    // ✅ Eliminar cliente
    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return "redirect:/lista/clientes?deleted";
    }

    // ✅ Filtrar clientes (AJAX)
    @GetMapping("/filtrar")
    @ResponseBody
    public ResponseEntity<List<ClienteDTO>> filtrarClientes(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false, name = "tipo") Long tipoDocumentoId,
            @RequestParam(required = false) String estado) {

        List<ClienteDTO> clientesFiltrados = clienteService.filtrarClientes(buscar, tipoDocumentoId, estado);
        return ResponseEntity.ok(clientesFiltrados);
    }
}
