package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.Repository.CarritoRepository;
import com.sistemaFacturacion.Mambo.Repository.PagoRepository;
import com.sistemaFacturacion.Mambo.Repository.ProductoRepository;
import com.sistemaFacturacion.Mambo.dto.CarritoDTO;
import com.sistemaFacturacion.Mambo.dto.DetalleCarritoDto;
import com.sistemaFacturacion.Mambo.model.Producto;
import com.sistemaFacturacion.Mambo.model.carrito;
import com.sistemaFacturacion.Mambo.model.detalleCarrito;
import com.sistemaFacturacion.Mambo.model.cliente;
import com.sistemaFacturacion.Mambo.model.pago;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final PagoRepository pagoRepository;
    private final ProductoRepository productoRepository;

    public CarritoService(CarritoRepository carritoRepository, PagoRepository pagoRepository, ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.pagoRepository = pagoRepository;
        this.productoRepository = productoRepository;
    }

    // 🧩 Convertir entidad -> DTO
    private CarritoDTO convertirADTO(carrito entidad) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(entidad.getId());
        if (entidad.getCliente() != null) {
            dto.setClienteId(entidad.getCliente().getId());
            dto.setNombreCliente(entidad.getCliente().getNombreCompleto());
        }
        dto.setFechaCreacion(entidad.getFechaCreacion());

        List<DetalleCarritoDto> detalles = entidad.getDetalles().stream().map(detalle -> {
            DetalleCarritoDto d = new DetalleCarritoDto();
            d.setId(detalle.getId());
            d.setProductoId(detalle.getProducto().getId());
            d.setNombreProducto(detalle.getProducto().getNombre());
            d.setPrecioUnitario(detalle.getProducto().getPrecio());
            d.setCantidad(detalle.getCantidad());
            d.setSubtotal(detalle.getSubTotal());
            return d;
        }).collect(Collectors.toList());

        dto.setDetalles(detalles);

        double total = detalles.stream().mapToDouble(DetalleCarritoDto::getSubtotal).sum();
        dto.setTotal(total);

        // ✅ Si tiene pago asociado, agregar info de pago al DTO (opcional)
        if (entidad.getPago() != null) {
            dto.setMetodoPago(entidad.getPago().getMetodo());
            dto.setEstadoPago(entidad.getPago().getEstado());
            dto.setPrecioPago(entidad.getPago().getPrecio());
        }

        return dto;
    }

    // 🧩 Convertir DTO -> entidad
    @Transactional
public carrito convertirAEntidad(CarritoDTO dto, cliente cliente) {
    carrito entidad = new carrito();
    entidad.setCliente(cliente);

    List<detalleCarrito> detalles = dto.getDetalles().stream().map(d -> {
        detalleCarrito det = new detalleCarrito();

        // Buscar producto en BD
        Producto producto = productoRepository.findById(d.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + d.getProductoId()));

        det.setProducto(producto);
        det.setCantidad(d.getCantidad());
        det.setCarrito(entidad);

        // Calcular subtotal
        det.setSubTotal(producto.getPrecio() * d.getCantidad());

        return det;
    }).collect(Collectors.toList());

    entidad.setDetalles(detalles);
    return entidad;
}

@Transactional
public CarritoDTO guardarCarrito(CarritoDTO carritoDTO, cliente cliente) {
    // Convertir DTO a entidad
    carrito entidad = convertirAEntidad(carritoDTO, cliente);

    // Guardar en BD (gracias al cascade se guardan detalles)
    carrito guardado = carritoRepository.save(entidad);

    // Convertir de nuevo a DTO para devolver
    CarritoDTO resultado = new CarritoDTO();
    resultado.setId(guardado.getId());
    resultado.setClienteId(guardado.getCliente().getId());
    resultado.setNombreCliente(guardado.getCliente().getNombreCompleto());
    resultado.setDetalles(
        guardado.getDetalles().stream().map(det -> {
            DetalleCarritoDto d = new DetalleCarritoDto();
            d.setId(det.getId());
            d.setProductoId(det.getProducto().getId());
            d.setNombreProducto(det.getProducto().getNombre());
            d.setPrecioUnitario(det.getProducto().getPrecio());
            d.setCantidad(det.getCantidad());
            d.setSubtotal(det.getSubTotal());
            return d;
        }).collect(Collectors.toList())
    );

    resultado.setTotal(
        resultado.getDetalles().stream().mapToDouble(DetalleCarritoDto::getSubtotal).sum()
    );

    return resultado;
}

    // ➕ Crear carrito (con detalles)
    public CarritoDTO crearCarrito(carrito carrito) {
        carrito nuevo = carritoRepository.save(carrito);
        return convertirADTO(nuevo);
    }

    // ✏️ Actualizar carrito
    public CarritoDTO actualizarCarrito(Long id, carrito carritoActualizado) {
        carrito actualizado = carritoRepository.findById(id)
                .map(c -> {
                    c.setCliente(carritoActualizado.getCliente());
                    c.getDetalles().clear();
                    c.getDetalles().addAll(carritoActualizado.getDetalles());
                    return carritoRepository.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con id " + id));

        return convertirADTO(actualizado);
    }

    // 🔎 Buscar carrito por id
    public Optional<CarritoDTO> obtenerCarritoPorId(Long id) {
        return carritoRepository.findById(id).map(this::convertirADTO);
    }

    // 📋 Listar todos los carritos
    public List<CarritoDTO> listarCarritos() {
        return carritoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ❌ Eliminar carrito
    public void eliminarCarrito(Long id) {
        carritoRepository.deleteById(id);
    }

    // 🔎 Buscar carritos por cliente
    public List<CarritoDTO> buscarPorCliente(Long clienteId) {
        return carritoRepository.findByClienteId(clienteId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // 🔎 Buscar carrito por comprobante
    public CarritoDTO buscarPorComprobante(Long comprobanteId) {
        carrito c = carritoRepository.findByComprobanteId(comprobanteId);
        return (c != null) ? convertirADTO(c) : null;
    }

    // ➕ Agregar un detalle a un carrito existente
    public CarritoDTO agregarDetalle(Long carritoId, detalleCarrito detalle) {
        carrito c = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con id " + carritoId));
        detalle.setCarrito(c);
        c.getDetalles().add(detalle);
        carrito actualizado = carritoRepository.save(c);
        return convertirADTO(actualizado);
    }

    // 💳 Registrar un pago para el carrito
    public pago registrarPago(Long carritoId, String metodoPago) {
        carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (carrito.getPago() != null) {
            throw new RuntimeException("Este carrito ya tiene un pago registrado");
        }

        double total = carrito.getDetalles().stream()
                .mapToDouble(detalleCarrito::getSubTotal)
                .sum();

        pago nuevoPago = new pago();
        nuevoPago.setPrecio(total);
        nuevoPago.setMetodo(metodoPago);
        nuevoPago.setEstado("Pendiente");
        nuevoPago.setFechaPago(LocalDateTime.now());

        pago guardado = pagoRepository.save(nuevoPago);
        carrito.setPago(guardado);
        carritoRepository.save(carrito);

        return guardado;
    }

    // 🔄 Actualizar estado de pago (ej. de Pendiente -> Completado)
    public pago actualizarEstadoPago(Long pagoId, String nuevoEstado) {
        pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        pago.setEstado(nuevoEstado);
        return pagoRepository.save(pago);
    }

    // 🔍 Obtener pago por carrito
    public pago obtenerPagoPorCarrito(Long carritoId) {
        carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        return carrito.getPago();
    }
}
