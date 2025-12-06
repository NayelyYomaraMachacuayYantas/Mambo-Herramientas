let productosFactura = [];

    function buscarRuc() {
      const ruc = document.getElementById('rucCliente').value;
      if (ruc.length === 11) {
        // Simulación de búsqueda RUC
        document.getElementById('razonSocial').value = 'Empresa Ejemplo S.A.C.';
        document.getElementById('direccionCliente').value = 'Av. Principal 123, Lima';
      }
    }

    function agregarProductoFactura() {
      const codigo = document.getElementById('codigoProductoF').value;
      const descripcion = document.getElementById('descripcionProducto').value;
      const cantidad = parseInt(document.getElementById('cantidadProductoF').value);
      const precio = parseFloat(document.getElementById('precioProductoF').value);

      if (!codigo || !descripcion || !cantidad || !precio) {
        alert('Por favor complete todos los campos del producto');
        return;
      }

      const valorVenta = cantidad * precio;
      const igv = valorVenta * 0.18;
      const importeTotal = valorVenta + igv;

      const producto = {
        codigo: codigo,
        descripcion: descripcion,
        cantidad: cantidad,
        precio: precio,
        valorVenta: valorVenta,
        igv: igv,
        importeTotal: importeTotal
      };

      productosFactura.push(producto);
      actualizarTablaFactura();
      calcularTotalesFactura();
      limpiarCamposProductoFactura();
    }

    function actualizarTablaFactura() {
      const tbody = document.querySelector('#tablaProductosFactura tbody');
      tbody.innerHTML = '';

      productosFactura.forEach((producto, index) => {
        const fila = `
          <tr>
            <td>${producto.codigo}</td>
            <td>${producto.descripcion}</td>
            <td>${producto.cantidad}</td>
            <td>S/ ${producto.precio.toFixed(2)}</td>
            <td>S/ ${producto.valorVenta.toFixed(2)}</td>
            <td>S/ ${producto.igv.toFixed(2)}</td>
            <td>S/ ${producto.importeTotal.toFixed(2)}</td>
            <td>
              <button type="button" class="btn btn-sm btn-outline-danger" onclick="eliminarProductoFactura(${index})">
                <i class="bi bi-trash"></i>
              </button>
            </td>
          </tr>
        `;
        tbody.innerHTML += fila;
      });
    }

    function eliminarProductoFactura(index) {
      productosFactura.splice(index, 1);
      actualizarTablaFactura();
      calcularTotalesFactura();
    }

    function calcularTotalesFactura() {
      const valorVenta = productosFactura.reduce((sum, producto) => sum + producto.valorVenta, 0);
      const igv = productosFactura.reduce((sum, producto) => sum + producto.igv, 0);
      const total = valorVenta + igv;

      document.getElementById('valorVentaFactura').textContent = `S/ ${valorVenta.toFixed(2)}`;
      document.getElementById('igvFactura').textContent = `S/ ${igv.toFixed(2)}`;
      document.getElementById('totalFactura').textContent = `S/ ${total.toFixed(2)}`;
    }

    function limpiarCamposProductoFactura() {
      document.getElementById('codigoProductoF').value = '';
      document.getElementById('descripcionProducto').value = '';
      document.getElementById('cantidadProductoF').value = '1';
      document.getElementById('precioProductoF').value = '';
    }

    function limpiarFormulario() {
      productosFactura = [];
      actualizarTablaFactura();
      calcularTotalesFactura();
      document.getElementById('rucCliente').value = '';
      document.getElementById('razonSocial').value = '';
      document.getElementById('direccionCliente').value = '';
      limpiarCamposProductoFactura();
    }

    function generarFactura() {
      if (productosFactura.length === 0) {
        alert('Debe agregar al menos un producto');
        return;
      }
      if (!document.getElementById('rucCliente').value) {
        alert('Debe ingresar el RUC del cliente');
        return;
      }
      alert('Factura generada correctamente');
    }

    function enviarSunat() {
      if (productosFactura.length === 0) {
        alert('Debe generar la factura primero');
        return;
      }
      alert('Enviando factura a SUNAT...');
    }

    function descargarPDF() {
      if (productosFactura.length === 0) {
        alert('Debe generar la factura primero');
        return;
      }
      alert('Descargando PDF de la factura...');
    }

    function guardarBorradorFactura() {
      alert('Borrador de factura guardado correctamente');
    }

    // Mostrar/ocultar campos según condición de pago
    document.getElementById('condicionPago').addEventListener('change', function() {
      const campoCredito = document.getElementById('campoPlazoCredito');
      if (this.value === 'credito') {
        campoCredito.style.display = 'block';
      } else {
        campoCredito.style.display = 'none';
      }
    });

    // Establecer fecha actual
    document.getElementById('fechaEmision').valueAsDate = new Date();
    
    // Establecer fecha de vencimiento (30 días después)
    const fechaVencimiento = new Date();
    fechaVencimiento.setDate(fechaVencimiento.getDate() + 30);
    document.getElementById('fechaVencimiento').valueAsDate = fechaVencimiento;