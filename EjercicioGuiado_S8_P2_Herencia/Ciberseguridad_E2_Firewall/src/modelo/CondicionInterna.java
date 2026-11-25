package modelo;

// Composición: esta clase NO es pública ni tiene constructor público,
// por eso solo se crea dentro de una Regla
class CondicionInterna {
    private String campo;
    private String valor;

    CondicionInterna(String campo, String valor) {
        this.campo = campo;
        this.valor = valor;
    }

    boolean esIgualA(CondicionInterna otra) {
        return campo.equals(otra.campo) && valor.equals(otra.valor);
    }

    String describir() {
        return campo + " = " + valor;
    }
}
