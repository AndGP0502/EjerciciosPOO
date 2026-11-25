package modelo;

public class AnalizadorVersiones {

    private VersionSoftware[] versiones;

    public AnalizadorVersiones(VersionSoftware[] versiones) {
        this.versiones = versiones;
    }

    // Contar versiones estables
    public int contarEstables() {

        int contador = 0;

        for (int i = 0; i < versiones.length; i++) {

            if (versiones[i] != null && versiones[i].isEstable()) {
                contador++;
            }
        }

        return contador;
    }

    // Buscar una version por numero
    public VersionSoftware buscarVersion(String numero) {

        for (int i = 0; i < versiones.length; i++) {

            if (versiones[i] != null &&
                    versiones[i].getNumero().equals(numero)) {

                return versiones[i];
            }
        }

        return null;
    }

    // Obtener la version con mas cambios
    public VersionSoftware obtenerVersionMasCambios() {

        VersionSoftware mayor = null;

        for (int i = 0; i < versiones.length; i++) {

            if (versiones[i] != null) {

                if (mayor == null ||
                        versiones[i].getCantidadCambios() >
                                mayor.getCantidadCambios()) {

                    mayor = versiones[i];
                }
            }
        }

        return mayor;
    }

    // Sumar todos los cambios
    public int sumarCambios() {

        int suma = 0;

        for (int i = 0; i < versiones.length; i++) {

            if (versiones[i] != null) {
                suma += versiones[i].getCantidadCambios();
            }
        }

        return suma;
    }

    // Desafio: calcular promedio de cambios
    public double calcularPromedioCambios() {

        int contador = 0;

        for (int i = 0; i < versiones.length; i++) {

            if (versiones[i] != null) {
                contador++;
            }
        }

        if (contador == 0) {
            return 0;
        }

        return (double) sumarCambios() / contador;
    }
}

