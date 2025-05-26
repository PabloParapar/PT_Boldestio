# Prueba Técnica para Boldest.io
Este repositorio contiene la prueba técnica para el puesto en prácticas en Boldest.io, en donde hago dos implementaciones de una misma aplicación, una con Java y la otra con Python.
<br/>La aplicación consiste en un script con el que traducir un archivo CSV dado a un archivo JSON.
## Instrucciones
Una vez exportado a su versión .jar podemos ejecutarlo por la terminal con `java -jar` en el caso de Java.
En el caso de Python exportaríamos una versión .exe que ejecutaríamos directamente sin depender de python.
Podemos ejecutar ambas versiones de 3 maneras distintas:
- 0 argumentos: Al no tener argumentos pedirá por terminal las rutas de los archivos de entrada y de salida.
- 1 argumento:  Al dar solo un argumento (ruta del archivo de entrada) devolverá el archivo de salida en la misma ruta que el de entrada con el mismo nombre y en el nuevo formato.
- 2 argumentos: Al dar los dos argumentos leerá el archivo de entrada según su ruta y devolverá el nuevo archivo a la nueva ruta. En caso de que se exporte a un directorio que no existe lo creará.
## Estructura del Repositorio
### [Proyecto de Java](https://github.com/PabloParapar/PT_Boldestio/tree/main/PT_Boldestio_Java)
El proyecto de Java está construido con Gradle y utiliza las siguientes dependencias:
- OpenCSV: para la lectura del archivo CSV.
- GSON: para la escritura del archivo JSON a partir de las instancias de las clases.
#### Ejemplo de uso
- `java -jar ConversorCsvAJson.jar` Pedirá por terminal la ruta de entrada y la ruta de salida.
- `java -jar ConversorCsvAJson.jar .\Entrada.csv` Escribirá el archivo Json en la misma ruta que la entrada y con el mismo nombre.
- `java -jar ConversorCsvAJson.jar .\Entrada.csv .\salida\Salida.json` Leerá el archivo de entrada en su ruta y escribirá el archivo de salida en la ruta específica.
### [Proyecto de Python](https://github.com/PabloParapar/PT_Boldestio/tree/main/PT_Boldestio_Python)
El proyecto de Python sigue la misma lógica de programación que el de Java, creando las clases necesarias con dataclasses.<br/>Utiliza las librerías nativas de csv y json para la lectura y escritura de los archivos.
#### Ejemplo de uso
- `ConversorCsvAJson.exe` Pedirá por terminal la ruta de entrada y la ruta de salida.
- `ConversorCsvAJson.exe Entrada.csv` Escribirá el archivo Json en la misma ruta que la entrada y con el mismo nombre.
- `ConversorCsvAJson.exe Entrada.csv salida\Salida.json` Leerá el archivo de entrada en su ruta y escribirá el archivo de salida en la ruta específica.
