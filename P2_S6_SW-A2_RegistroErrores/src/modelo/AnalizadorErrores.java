package modelo;

public class AnalizadorErrores {

    private ErrorSoftware[] errores;

    public AnalizadorErrores(ErrorSoftware[] errores) {
        this.errores = errores;
    }

    // Contar errores pendientes
    public int contarPendientes() {

        int contador = 0;

        for (int i = 0; i < errores.length; i++) {

            if (errores[i] != null && !errores[i].isResuelto()) {
                contador++;
            }
        }

        return contador;
    }

    // Buscar un error por codigo
    public ErrorSoftware buscarPorCodigo(String codigo) {

        for (int i = 0; i < errores.length; i++) {

            if (errores[i] != null &&
                    errores[i].getCodigo().equals(codigo)) {

                return errores[i];
            }
        }

        return null;
    }

    // Listar errores por severidad
    public String listarPorSeveridad(String severidad) {

        String resultado = "";

        for (int i = 0; i < errores.length; i++) {

            if (errores[i] != null &&
                    errores[i].getSeveridad().equals(severidad)) {

                resultado += errores[i].mostrarInformacion() + "\n";
            }
        }

        return resultado;
    }

    // Contar errores por severidad
    public int contarPorSeveridad(String severidad) {

        int contador = 0;

        for (int i = 0; i < errores.length; i++) {

            if (errores[i] != null &&
                    errores[i].getSeveridad().equals(severidad)) {

                contador++;
            }
        }

        return contador;
    }

    // Desafio adicional
    public ErrorSoftware obtenerPrimerPendienteAlta() {

        for (int i = 0; i < errores.length; i++) {

            if (errores[i] != null &&
                    errores[i].getSeveridad().equals("ALTA") &&
                    !errores[i].isResuelto()) {

                return errores[i];
            }
        }

        return null;
    }
}
