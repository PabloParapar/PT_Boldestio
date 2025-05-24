package script;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException, CsvException {

        ///Leer archivo CSV
        String archivoCsv = "D:/pablo/Trabajo/Entrevistas/25_05_Boldest/00_SourceFiles/Entrada.csv";

        CSVReader reader = new CSVReader(new FileReader(archivoCsv));

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

        Files.write(Paths.get("salida.json"), json.getBytes(StandardCharsets.US_ASCII));
        System.out.println(json);
        System.out.println("Salida generada con éxito");
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