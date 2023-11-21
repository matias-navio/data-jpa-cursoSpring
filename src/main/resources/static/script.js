document.addEventListener('DOMContentLoaded', function () {
    var cerrarAlertaBtns = document.querySelectorAll('.btn-cerrar');

    cerrarAlertaBtns.forEach(function (btn) {
        btn.addEventListener('click', function () {
            var alerta1 = this.closest('.alerta-creado');
            var alerta2 = this.closest('.alerta-error');
            var alerta3 = this.closest('.alerta-foto');

            if(alerta1){
                alerta1.style.display = 'none';
            }
            if(alerta2){
                alerta2.style.display = 'none';
            }
            if(alerta3){
                alerta3.style.display = 'none';
            }

        });
    });
});
