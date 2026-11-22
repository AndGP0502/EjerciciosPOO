# Puente Estudiantil

Aplicación de escritorio (Java + Swing) para que el DECE de un colegio fiscal detecte y dé seguimiento a estudiantes en situación de pobreza con riesgo de abandono escolar.

Proyecto de Programación II (ISWZ1103) – UDLA.
Equipo: André Garzón (líder), Mateo Salgado, José Ayora y Juyani.

## Cómo abrirlo en IntelliJ IDEA

1. En IntelliJ, elija **File > Open** y seleccione la carpeta `PuenteEstudiantil`.
2. Si aparece el aviso *Project SDK is not defined*, elija su JDK (17 o superior).
3. Si la carpeta `src` no aparece en azul, haga clic derecho sobre ella y elija **Mark Directory as > Sources Root**. Haga lo mismo con `test`, pero eligiendo **Test Sources Root**.
4. Para abrir el programa, ejecute `src/interfaz/Main.java` (clic derecho > Run 'Main.main()').
5. Para correr las pruebas, ejecute `test/pruebas/EjecutarPruebas.java`. Debe mostrar 119 pruebas aprobadas y 0 fallidas.

También puede abrir el programa con doble clic en `PuenteEstudiantil.jar`, o desde la terminal con `java -jar PuenteEstudiantil.jar`.

## Estructura

```
src/
  modelo/     Entidades del mundo del problema (Estudiante, ProgramaApoyo, CasoSeguimiento, ...)
  negocio/    Reglas de negocio, un gestor por módulo, y PuenteException
  interfaz/   Ventanas y formularios Swing (Main es el punto de entrada)
test/
  pruebas/    Pruebas de los módulos, sin librerías externas
```

## Estado de los módulos

| Módulo | Responsable | Estado |
|---|---|---|
| 1. Estudiantes y riesgo de abandono (RF-01 a RF-03) | André Garzón | Terminado y probado |
| 2. Programas de apoyo (RF-04 a RF-06) | Mateo Salgado | Terminado y probado |
| 3. Casos de seguimiento (RF-07 a RF-09) | José Ayora | Terminado y probado |
| 4. Alertas y reportes (RF-10 a RF-12) | Juyani | Estructura base: `GestorAlertas` ya tiene sus atributos y métodos; falta la lógica (ver los TODO) y la pantalla |
| 5. Rendimiento académico (RF-13 a RF-15) | André Garzón | Terminado y probado |

## Datos

Por ahora los datos se guardan en memoria (listas `ArrayList`). Al abrir el programa se cargan datos de ejemplo ficticios (`negocio/DatosDeEjemplo.java`) para poder probar todas las pantallas. Al cerrar el programa, los cambios se pierden.
