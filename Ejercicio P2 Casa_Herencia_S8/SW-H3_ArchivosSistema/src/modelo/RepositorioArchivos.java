package modelo;

import java.util.ArrayList;

// Agregación: el repositorio guarda archivos creados por fuera (en Main)
public class RepositorioArchivos {
    private ArrayList<Archivo> archivos;

    public RepositorioArchivos() {
        this.archivos = new ArrayList<>();
    }

    public void agregarArchivo(Archivo archivo) {
        archivos.add(archivo);
    }

    public int getCantidadArchivos() {
        return archivos.size();
    }

    // Retorna el listado completo como String (la clase no imprime)
    public String listarArchivos() {
        String lista = "";
        for (Archivo a : archivos) {
            lista += a.mostrarInformacion() + "\n";
        }
        return lista;
    }

    public double calcularEspacioTotalProcesado() {
        double total = 0;
        for (Archivo a : archivos) {
            total += a.calcularEspacioProcesado();
        }
        return total;
    }
}
