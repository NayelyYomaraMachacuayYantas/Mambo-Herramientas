package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.Repository.CarritoRepository;
import com.sistemaFacturacion.Mambo.Repository.DetalleCarritoRepository;
import com.sistemaFacturacion.Mambo.Repository.ProductoRepository;
import com.sistemaFacturacion.Mambo.dto.CarritoDTO;
import com.sistemaFacturacion.Mambo.dto.DetalleCarritoDto;
import com.sistemaFacturacion.Mambo.model.carrito;
import com.sistemaFacturacion.Mambo.model.cliente;
import com.sistemaFacturacion.Mambo.model.detalleCarrito;
import com.sistemaFacturacion.Mambo.model.Producto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DetalleCarritoService {

    private final DetalleCarritoRepository detalleCarritoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    public DetalleCarritoService(DetalleCarritoRepository detalleCarritoRepository,
                                 CarritoRepository carritoRepository, ProductoRepository productoRepository) {
        this.detalleCarritoRepository = detalleCarritoRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    // 🔁 Conversión entidad -> DTO
    private DetalleCarritoDto convertirADTO(detalleCarrito entidad) {
        DetalleCarritoDto dto = new DetalleCarritoDto();
        dto.setId(entidad.getId());
        dto.setProductoId(entidad.getProducto().getId());
        dto.setNombreProducto(entidad.getProducto().getNombre());
        dto.setPrecioUnitario(entidad.getProducto().getPrecio());
        dto.setCantidad(entidad.getCantidad());
        dto.setSubtotal(entidad.getSubTotal());
        return dto;
    }

    // 🔁 Conversión DTO -> entidad (requiere carrito y producto cargados)
    private carrito convertirAEntidad(CarritoDTO dto, cliente cliente) {
    carrito entidad = new carrito();
    entidad.setCliente(cliente);

    List<detalleCarrito> detalles = dto.getDetalles().stream().map(d -> {
        detalleCarrito det = new detalleCarrito();
        Producto producto = productoRepository.findById(d.getProductoId())
        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + d.getProductoId()));

    det.setProducto(producto); // o buscar en BD
        det.setCantidad(d.getCantidad());
        det.setCarrito(entidad);
        return det;
    }).collect(Collectors.toList());

    entidad.setDetalles(detalles);
    return entidad;
}


    // ✅ Recalcular total del carrito
    private void actualizarTotalCarrito(carrito carrito) {
        double total = carrito.getDetalles().stream()
                .mapToDouble(detalleCarrito::getSubTotal)
                .sum();
        carritoRepository.save(carrito);
    }

    // ➕ Crear detalle
    public DetalleCarritoDto crearDetalle(detalleCarrito detalle) {
        detalleCarrito nuevo = detalleCarritoRepository.save(detalle);
        actualizarTotalCarrito(nuevo.getCarrito());
        return convertirADTO(nuevo);
    }

    // ✏️ Actualizar detalle
    public DetalleCarritoDto actualizarDetalle(Long id, detalleCarrito detalle) {
        detalleCarrito actualizado = detalleCarritoRepository.findById(id)
                .map(existing -> {
                    existing.setProducto(detalle.getProducto());
                    existing.setCantidad(detalle.getCantidad());
                    existing.setCarrito(detalle.getCarrito());
                    detalleCarrito guardado = detalleCarritoRepository.save(existing);
                    actualizarTotalCarrito(guardado.getCarrito());
                    return guardado;
                })
                .orElseThrow(() -> new RuntimeException("Detalle de carrito no encontrado con ID: " + id));

        return convertirADTO(actualizado);
    }

    // ❌ Eliminar detalle
    public void eliminarDetalle(Long id) {
        detalleCarritoRepository.findById(id).ifPresent(detalle -> {
            carrito carrito = detalle.getCarrito();
            detalleCarritoRepository.deleteById(id);
            actualizarTotalCarrito(carrito);
        });
    }

    // 🔍 Buscar por ID
    public Optional<DetalleCarritoDto> obtenerPorId(Long id) {
        return detalleCarritoRepository.findById(id)
                .map(this::convertirADTO);
    }

    // 📋 Listar todos
    public List<DetalleCarritoDto> listarDetalles() {
        return detalleCarritoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 📋 Listar por carrito
    public List<DetalleCarritoDto> listarPorCarrito(Long carritoId) {
        return detalleCarritoRepository.findByCarritoId(carritoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 📋 Listar por producto
    public List<DetalleCarritoDto> listarPorProducto(Long productoId) {
        return detalleCarritoRepository.findByProductoId(productoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
private CarritoDTO convertirADTO(carrito entidad) {
    CarritoDTO dto = new CarritoDTO();
    dto.setId(entidad.getId());
    dto.setClienteId(entidad.getCliente().getId());
    dto.setTotal(entidad.getDetalles().stream()
            .mapToDouble(det -> det.getProducto().getPrecio() * det.getCantidad())
            .sum());

    // Convertir los detalles
    List<DetalleCarritoDto> detallesDTO = entidad.getDetalles().stream()
            .map(this::convertirADTO) // reutiliza tu método existente
            .collect(Collectors.toList());
    dto.setDetalles(detallesDTO);

    return dto;
}


    @Transactional
public CarritoDTO guardarCarrito(CarritoDTO dto, cliente cliente) {
    // 1️⃣ Crear la entidad carrito
    carrito carritoEntidad = convertirAEntidad(dto, cliente);

    // 2️⃣ Guardar carrito + detalles (cascade ALL se encarga de los detalles)
    carrito guardado = carritoRepository.save(carritoEntidad);

    // 3️⃣ Calcular y actualizar subtotal total
    guardado.getDetalles().forEach(det -> {
        det.setSubTotal(det.getProducto().getPrecio() * det.getCantidad());
    });
    carritoRepository.save(guardado);

    // 4️⃣ Convertir a DTO y devolver
    return convertirADTO(guardado);
}

}
