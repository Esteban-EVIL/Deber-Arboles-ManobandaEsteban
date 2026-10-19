# Catalogo Biblioteca FISEI

Implementacion en Java de un arbol general para la jerarquia de la biblioteca y de un ABB independiente para los libros de cada estante.

## Requisitos

- JDK 8 o superior instalado y disponible en `PATH` (`java` y `javac`).
- Ejecutar los comandos desde la carpeta raiz del proyecto.

## Compilar y ejecutar

En PowerShell:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })
java -cp out app.Main
```

Para ejecutar los casos de prueba:

```powershell
java -cp out app.PruebasBiblioteca
```

La salida final esperada de las pruebas es `14/14 pruebas OK`.

## Estructura

- `src/enums`: tipos de unidad y recorrido.
- `src/modelo`: libro y unidad bibliotecaria.
- `src/negocio`: nodos y operaciones de los arboles.
- `src/app`: menu, datos iniciales y pruebas ejecutables.
- `TRAZAS.md`: recorridos, estructura del ABB y eliminaciones resueltas.
