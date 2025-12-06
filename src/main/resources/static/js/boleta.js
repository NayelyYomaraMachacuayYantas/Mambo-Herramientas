let productosCarrito = [];

    function seleccionarProducto(codigo, nombre, precio) {
      document.getElementById('codigoProducto').value = codigo;
      document.getElementById('precioProducto').value = precio;
    }

    function agregarProducto() {
      const codigo = document.getElementById('codigoProducto').value;
      const cantidad = parseInt(document.getElementById('cantidadProducto').value);
      const precio = parseFloat(document.getElementById('precioProducto').value);

      if (!codigo || !cantidad || !precio) {
        alert('Por favor complete todos los campos del producto');
        return;
      }

      const producto = {
        codigo: codigo,
        nombre: codigo === 'R001' ? 'Camiseta Hombre' : 
                codigo === 'R002' ? 'Pantalon Mujer' : 
                codigo === 'R003' ? 'Chaqueta Unisex' : 'Producto',
        cantidad: cantidad,
        precio: precio,
        subtotal: cantidad * precio
      };

      productosCarrito.push(producto);
      actualizarTablaProductos();
      calcularTotales();
      limpiarCamposProducto();
    }

    function actualizarTablaProductos() {
      const tbody = document.querySelector('#tablaProductos tbody');
      tbody.innerHTML = '';

      productosCarrito.forEach((producto, index) => {
        const fila = `
          <tr>
            <td>${producto.codigo}</td>
            <td>${producto.nombre}</td>
            <td>${producto.cantidad}</td>
            <td>S/ ${producto.precio.toFixed(2)}</td>
            <td>S/ ${producto.subtotal.toFixed(2)}</td>
            <td>
              <button type="button" class="btn btn-sm btn-outline-danger" onclick="eliminarProducto(${index})">
                <i class="bi bi-trash"></i>
              </button>
            </td>
          </tr>
        `;
        tbody.innerHTML += fila;
      });
    }

    function eliminarProducto(index) {
      productosCarrito.splice(index, 1);
      actualizarTablaProductos();
      calcularTotales();
    }

    function calcularTotales() {
      const subtotal = productosCarrito.reduce((sum, producto) => sum + producto.subtotal, 0);
      const igv = subtotal * 0.18;
      const total = subtotal + igv;

      document.getElementById('subtotalVenta').textContent = `S/ ${subtotal.toFixed(2)}`;
      document.getElementById('igvVenta').textContent = `S/ ${igv.toFixed(2)}`;
      document.getElementById('totalVenta').textContent = `S/ ${total.toFixed(2)}`;
    }

    function limpiarCamposProducto() {
      document.getElementById('codigoProducto').value = '';
      document.getElementById('cantidadProducto').value = '1';
      document.getElementById('precioProducto').value = '';
    }

    function limpiarFormulario() {
      productosCarrito = [];
      actualizarTablaProductos();
      calcularTotales();
      document.getElementById('numDocCliente').value = '';
      document.getElementById('nombreCliente').value = '';
      limpiarCamposProducto();
    }

    function generarBoleta() {
      if (productosCarrito.length === 0) {
        alert('Debe agregar al menos un producto');
        return;
      }
      alert('Boleta generada correctamente');
    }

    function verPreview() {
      if (productosCarrito.length === 0) {
        alert('Debe agregar al menos un producto para ver la vista previa');
        return;
      }
      alert('Abriendo vista previa de la boleta...');
    }

    function guardarBorrador() {
      alert('Borrador guardado correctamente');
    }

    // Calcular vuelto automáticamente
    document.getElementById('montoRecibido').addEventListener('input', function() {
      const total = parseFloat(document.getElementById('totalVenta').textContent.replace('S/ ', ''));
      const recibido = parseFloat(this.value) || 0;
      const vuelto = recibido - total;
      
      if (vuelto >= 0) {
        document.getElementById('montoVuelto').textContent = `S/ ${vuelto.toFixed(2)}`;
        document.getElementById('campoVuelto').style.display = 'block';
      } else {
        document.getElementById('campoVuelto').style.display = 'none';
      }
    });

    // Mostrar/ocultar campos según método de pago
    document.getElementById('metodoPago').addEventListener('change', function() {
      const campoEfectivo = document.getElementById('campoEfectivo');
      if (this.value === 'efectivo' || this.value === 'mixto') {
        campoEfectivo.style.display = 'block';
      } else {
        campoEfectivo.style.display = 'none';
        document.getElementById('campoVuelto').style.display = 'none';
      }
    });