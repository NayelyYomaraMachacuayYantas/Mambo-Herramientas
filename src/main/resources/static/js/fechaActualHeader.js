function fechaActual() {
    const fecha = new Date();
    const dia = String(fecha.getDate()).padStart(2, '0');
    const meses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    const mesNombre =  meses[fecha.getMonth()]
    const anio = fecha.getFullYear();
    const horas = String(fecha.getHours()).padStart(2, '0');
    const minutos = String(fecha.getMinutes()).padStart(2, '0');
    const segundos = String(fecha.getSeconds()).padStart(2, '0');
    document.getElementById('fecha-actual').textContent = `${horas}:${minutos}:${segundos} | ${dia} ${mesNombre} de ${anio} `;
}