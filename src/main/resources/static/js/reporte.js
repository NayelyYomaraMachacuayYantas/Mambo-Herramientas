let graficoVentas;

    // Inicializar gráfico
    document.addEventListener('DOMContentLoaded', function() {
      inicializarGrafico();
      establecerFechasDefault();
    });

    function inicializarGrafico() {
      const ctx = document.getElementById('graficoVentas').getContext('2d');
      
      graficoVentas = new Chart(ctx, {
        type: 'line',
        data: {
          labels: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
          datasets: [{
            label: 'Ventas (S/)',
            data: [1200, 1900, 3000, 2500, 2200, 3200, 2800],
            borderColor: 'rgb(75, 192, 192)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            tension: 0.1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });
    }

    function cambiarTipoReporte() {
      const tipo = document.getElementById('tipoReporte').value;
      const tituloGrafico = document.getElementById('tituloGrafico');
      const tituloTabla = document.getElementById('tituloTabla');
      const headerTabla = document.getElementById('headerTabla');

      switch(tipo) {
        case 'ventas':
          tituloGrafico.textContent = 'Ventas por Día';
          tituloTabla.textContent = 'Detalle de Ventas';
          headerTabla.innerHTML = `
            <tr>
              <th>Producto</th>
              <th>Código</th>
              <th>Cantidad Vendida</th>
              <th>Total Ventas</th>
              <th>Stock Actual</th>
            </tr>
          `;
          break;
        case 'clientes':
          tituloGrafico.textContent = 'Clientes Más Frecuentes';
          tituloTabla.textContent = 'Ranking de Clientes';
          headerTabla.innerHTML = `
            <tr>
              <th>Cliente</th>
              <th>Documento</th>
              <th>Compras</th>
              <th>Total Gastado</th>
              <th>Última Compra</th>
            </tr>
          `;
          break;
        case 'vendedores':
          tituloGrafico.textContent = 'Rendimiento de Vendedores';
          tituloTabla.textContent = 'Performance de Vendedores';
          headerTabla.innerHTML = `
            <tr>
              <th>Vendedor</th>
              <th>Ventas Realizadas</th>
              <th>Total Vendido</th>
              <th>Promedio por Venta</th>
              <th>Meta Alcanzada</th>
            </tr>
          `;
          break;
      }
      generarReporte();
    }

    function generarReporte() {
      const tipo = document.getElementById('tipoReporte').value;
      const periodo = document.getElementById('periodoReporte').value;
      
      // Actualizar datos del gráfico según el tipo de reporte
      actualizarGrafico(tipo);
      actualizarTabla(tipo);
      
      console.log(`Generando reporte de ${tipo} para el período ${periodo}`);
    }

    function actualizarGrafico(tipo) {
      let datos, etiquetas, label;
      
      switch(tipo) {
        case 'ventas':
          etiquetas = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];
          datos = [1200, 1900, 3000, 2500, 2200, 3200, 2800];
          label = 'Ventas (S/)';
          break;
        case 'productos':
          etiquetas = ['Camiseta Hombre', 'Pantalon Mujer', 'Chaqueta Unisex', 'Zapatillas Deportivas', 'Gorra Casual'];
          datos = [49.90, 89.50, 129.90, 199, 39.90];
          label = 'Unidades Vendidas';
          break;
        case 'clientes':
          etiquetas = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'];
          datos = [85, 92, 108, 134, 142, 127];
          label = 'Clientes Activos';
          break;
        case 'vendedores':
          etiquetas = ['Carlos M.', 'Roberto S.', 'María G.', 'Ana T.', 'José P.'];
          datos = [3450, 3120, 2890, 2345, 1780];
          label = 'Ventas (S/)';
          break;
      }
      
      graficoVentas.data.labels = etiquetas;
      graficoVentas.data.datasets[0].data = datos;
      graficoVentas.data.datasets[0].label = label;
      graficoVentas.update();
    }

    function actualizarTabla(tipo) {
      const bodyTabla = document.getElementById('bodyTabla');
      let contenido = '';
      
      switch(tipo) {
        case 'ventas':
          contenido = `
            <tr><td>09/09/2025</td><td>B001-123</td><td>Juan Pérez</td><td>Carlos Mendoza</td><td>S/ 450.00</td></tr>
            <tr><td>09/09/2025</td><td>F001-456</td><td>Empresa ABC S.A.C.</td><td>María González</td><td>S/ 1,245.60</td></tr>
            <tr><td>08/09/2025</td><td>B001-124</td><td>Ana Torres</td><td>José Pérez</td><td>S/ 78.50</td></tr>
            <tr><td>08/09/2025</td><td>B001-125</td><td>Roberto Silva</td><td>Carlos Mendoza</td><td>S/ 234.90</td></tr>
            <tr><td>07/09/2025</td><td>F001-457</td><td>Distribuidora XYZ E.I.R.L.</td><td>María González</td><td>S/ 890.30</td></tr>
          `;
          break;
        case 'productos':
          contenido = `
            <tr><td>Camiseta Hombre</td><td>P001</td><td>450</td><td>S/ 2,450.00</td><td>45</td></tr>
            <tr><td>Pantalon Mujer</td><td>P002</td><td>380</td><td>S/ 1,890.00</td><td>23</td></tr>
            <tr><td>Chaqueta Unisex</td><td>P005</td><td>320</td><td>S/ 1,234.00</td><td>67</td></tr>
            <tr><td>Zapatilla Deportiva </td><td>P003</td><td>280</td><td>S/ 890.00</td><td>8</td></tr>
            <tr><td>Gorra Casual</td><td>P004</td><td>210</td><td>S/ 567.00</td><td>0</td></tr>
          `;
          break;
        case 'clientes':
          contenido = `
            <tr><td>Empresa ABC S.A.C.</td><td>20123456789</td><td>15</td><td>S/ 4,567.89</td><td>09/09/2025</td></tr>
            <tr><td>Juan Carlos Pérez</td><td>12345678</td><td>8</td><td>S/ 2,345.60</td><td>08/09/2025</td></tr>
            <tr><td>Distribuidora XYZ E.I.R.L.</td><td>20987654321</td><td>12</td><td>S/ 3,890.30</td><td>07/09/2025</td></tr>
            <tr><td>María González</td><td>87654321</td><td>6</td><td>S/ 1,234.50</td><td>06/09/2025</td></tr>
            <tr><td>Ana María Quispe</td><td>11223344</td><td>4</td><td>S/ 890.75</td><td>05/09/2025</td></tr>
          `;
          break;
        case 'vendedores':
          contenido = `
            <tr><td>Carlos Mendoza López</td><td>45</td><td>S/ 3,450.00</td><td>S/ 76.67</td><td>115%</td></tr>
            <tr><td>Roberto Silva Vargas</td><td>42</td><td>S/ 3,120.00</td><td>S/ 74.29</td><td>104%</td></tr>
            <tr><td>María González Ríos</td><td>38</td><td>S/ 2,890.00</td><td>S/ 76.05</td><td>96%</td></tr>
            <tr><td>Ana Lucía Torres</td><td>31</td><td>S/ 2,345.00</td><td>S/ 75.65</td><td>78%</td></tr>
            <tr><td>José Antonio Pérez</td><td>24</td><td>S/ 1,780.00</td><td>S/ 74.17</td><td>59%</td></tr>
          `;
          break;
      }
      
      bodyTabla.innerHTML = contenido;
    }

    function establecerFechasDefault() {
      const hoy = new Date();
      const inicioMes = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
      
      document.getElementById('fechaInicio').valueAsDate = inicioMes;
      document.getElementById('fechaFin').valueAsDate = hoy;
    }

    function limpiarFiltros() {
      document.getElementById('tipoReporte').value = 'ventas';
      document.getElementById('periodoReporte').value = 'mes';
      establecerFechasDefault();
      generarReporte();
    }

    function exportarExcel() {
      const tipo = document.getElementById('tipoReporte').value;
      alert(`Exportando reporte de ${tipo} a Excel...`);
      // Aquí implementarías la lógica para exportar a Excel
    }

    function exportarPDF() {
      const tipo = document.getElementById('tipoReporte').value;
      alert(`Exportando reporte de ${tipo} a PDF...`);
      // Aquí implementarías la lógica para exportar a PDF
    }

    // Cambiar filtros según el período seleccionado
    document.getElementById('periodoReporte').addEventListener('change', function() {
      const periodo = this.value;
      const fechaInicio = document.getElementById('fechaInicio');
      const fechaFin = document.getElementById('fechaFin');
      const hoy = new Date();
      
      switch(periodo) {
        case 'hoy':
          fechaInicio.valueAsDate = hoy;
          fechaFin.valueAsDate = hoy;
          break;
        case 'semana':
          const inicioSemana = new Date(hoy.setDate(hoy.getDate() - hoy.getDay()));
          fechaInicio.valueAsDate = inicioSemana;
          fechaFin.valueAsDate = new Date();
          break;
        case 'mes':
          const inicioMes = new Date(new Date().getFullYear(), new Date().getMonth(), 1);
          fechaInicio.valueAsDate = inicioMes;
          fechaFin.valueAsDate = new Date();
          break;
        case 'trimestre':
          const mes = new Date().getMonth();
          const inicioTrimestre = new Date(new Date().getFullYear(), Math.floor(mes/3)*3, 1);
          fechaInicio.valueAsDate = inicioTrimestre;
          fechaFin.valueAsDate = new Date();
          break;
        case 'año':
          const inicioAño = new Date(new Date().getFullYear(), 0, 1);
          fechaInicio.valueAsDate = inicioAño;
          fechaFin.valueAsDate = new Date();
          break;
      }
      
      if (periodo !== 'personalizado') {
        generarReporte();
      }
    });