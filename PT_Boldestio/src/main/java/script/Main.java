package script;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, CsvException {
        try {
            ///Leer archivo CSV
            String rutaCsv = "";
            String rutaJson = "";
            Path archivoCsv;
            Path archivoJson;
            switch (args.length) {
                case 0 -> {
                    Scanner entrada = new Scanner(System.in);
                    System.out.println("Conversor de CSV a JSON");
                    System.out.println("Ingresa la ruta del archivo CSV a traducir a JSON");
                    rutaCsv = entrada.next();
                    System.out.println("Ingresa la ruta del archivo JSON a exportar");
                    rutaJson = entrada.next();
                    entrada.close();
                }
                case 1 -> {
                    rutaCsv = args[0];
                    String nombreSinExtension = Paths.get(rutaCsv).getFileName().toString().replaceFirst("[.][^.]+$", "");
                    archivoJson = Paths.get(rutaCsv).resolveSibling(nombreSinExtension + ".json");
                    rutaJson = archivoJson.toString();
                }
                case 2 -> {
                    rutaCsv = args[0];
                    rutaJson = args[1];
                }
                default -> {
                    System.out.println("Uso incorrecto, debes incluir 0, 1 o 2 argumentos");
                    return;
                }
            }

            archivoCsv = Paths.get(rutaCsv);
            archivoJson = Paths.get(rutaJson);

            if (!Files.exists(archivoCsv)) {
                System.out.println("El archivo no existe: " + rutaCsv);
                System.exit(1);
            }

            System.out.println("Archivo recibido: " + rutaCsv);

            CSVReader reader = new CSVReader(new FileReader(String.valueOf(archivoCsv)));


            String[] header = reader.readNext();

            ///Crear instancias a partir de CSV

            Validator jsonSchema = new Validator("https://explora.handmadeexperiences.com/api/bw/v1/sync/schema.multiple.json");
            String[] fila;
            Tour ultimoTour = null;
            Day ultimoDay = null;
            while ((fila = reader.readNext()) != null) {
                String tipo = (fila[0].isEmpty()) ? null : fila[0];
                String id = (fila[1].isEmpty()) ? null : fila[1];
                String subrecurso = (fila[2].isEmpty()) ? null : fila[2];
                String day = (fila[3].isEmpty()) ? null : fila[3];
                String title = (fila[4].isEmpty()) ? null : escapeNonAscii(fila[4]);
                String htmlDescription = (fila[5].isEmpty()) ? null : escapeNonAscii(fila[5]);
                String htmlContent = (fila[6].isEmpty()) ? null : escapeNonAscii(fila[6]);
                String externalUrl = (fila[7].isEmpty()) ? null : fila[7];
                String contactCity = (fila[8].isEmpty()) ? null : escapeNonAscii(fila[8]);
                String contactProvince = (fila[9].isEmpty()) ? null : escapeNonAscii(fila[9]);
                String locationLat = (fila[10].isEmpty()) ? null : fila[10];
                String locationLon = (fila[11].isEmpty()) ? null : fila[11];
                switch (tipo) {
                    case "TOUR":
                        Tour nuevoTour = new Tour(id, title, htmlDescription, htmlContent);
                        ultimoTour = nuevoTour;
                        jsonSchema.annadirTour(nuevoTour);
                        break;
                    case "DAY":
                        Day nuevoDay = new Day("D" + day, title, htmlDescription, htmlContent);
                        ultimoDay = nuevoDay;
                        ultimoTour.annadirDay(nuevoDay);
                        break;
                    case "PLACE":
                        Place nuevoPlace = new Place(id, subrecurso, day, title, htmlDescription, htmlContent, locationLat, locationLon);
                        ultimoDay.annadirPlace(nuevoPlace);
                        break;
                }

            }

            ///Imprimir JSON a partir de Instancias

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            String jsonConTildes = gson.toJson(jsonSchema);
            String json = escapeNonAscii(jsonConTildes).replaceAll("\\\\u", "\\u");

            ///Crear el directorio de salida en caso de ser necesario

            Path rutaSalida = archivoJson;

            Path carpetaDestino = rutaSalida.getParent();
            if (carpetaDestino != null && !Files.exists(carpetaDestino)) {
                Files.createDirectories(carpetaDestino);
            }

            ///Escribir el archivo .json
            Files.write(rutaSalida, json.getBytes(StandardCharsets.US_ASCII));
            //System.out.println(json);
            System.out.println("Salida generada con éxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Se ha producido un error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // visible solo si lo ejecutas desde consola
        }
    }

    public static String escapeNonAscii(String entrada) {
        StringBuilder resultado = new StringBuilder();
        for (char c : entrada.toCharArray()) {
            if (c < 128) {
                resultado.append(c);
            } else {
                resultado.append(String.format("\\u%04x", (int) c));
            }
        }
        return resultado.toString();
    }
}