package modelo;

// Composición: esta clase NO es pública ni tiene constructor público,
// por eso solo puede existir dentro de un EventoRed
class PaqueteInspeccionado {
    private String ipOrigen;
    private String ipDestino;
    private int puerto;
    private int tamanoBytes;

    PaqueteInspeccionado(String ipOrigen, String ipDestino, int puerto, int tamanoBytes) {
        this.ipOrigen = ipOrigen;
        this.ipDestino = ipDestino;
        this.puerto = puerto;
        this.tamanoBytes = tamanoBytes;
    }

    String describir() {
        return "Origen: " + ipOrigen + " -> Destino: " + ipDestino
                + " | Puerto: " + puerto + " | Tamano: " + tamanoBytes + " bytes";
    }
}
