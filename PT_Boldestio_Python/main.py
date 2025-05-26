from dataclasses import dataclass, field, asdict, is_dataclass
from typing import List
from models import InfoContainer, Tour, Day, Place
from pathlib import Path
import argparse
import csv
import json

def main():
    # Indicar ruta del archivo CSV
    parser = argparse.ArgumentParser(description="Procesa un archivo CSV y lo traduce a JSON.")
    ## Añadir argumentos para la entrada y salida de archivos
    parser.add_argument('entrada_csv', nargs='?', type=str, help='Ruta del archivo CSV de entrada')
    parser.add_argument('salida_json', nargs='?', type=str, help='Ruta del archivo JSON de salida')
    args = parser.parse_args()

    # Obtener las rutas de entrada y salida
    if args.entrada_csv is None and args.salida_json is None:
        print("Convertidor de CSV a JSON")
        entrada_csv = input("Introduce la ruta del archivo CSV de entrada: ").strip()
        salida_json = input("Introduce la ruta del archivo JSON de salida (dejar en blanco para usar el mismo nombre que el CSV): ").strip()
    elif args.entrada_csv is not None and args.salida_json is None:
        entrada_csv = args.entrada_csv
        salida_json = entrada_csv.replace('.csv', '.json')
        print(f"No se proporcionó salida. Usando '{salida_json}' como nombre del archivo JSON.")

    else:
        entrada_csv = args.entrada_csv
        salida_json = args.salida_json        
    
    # Verificar que el archivo de entrada es un CSV
    if not entrada_csv.endswith('.csv'):
        raise ValueError("El archivo de entrada debe ser un CSV.")
    #Crear instancia de InfoContainer
    info_container = instanciar_datos_desde_csv(entrada_csv)

    # Guardar InfoContainer en un archivo JSON
    exportar_json(info_container, salida_json)


def instanciar_datos_desde_csv(entrada_csv):
    mySchema = "https://explora.handmadeexperiences.com/api/bw/v1/sync/schema.multiple.json" # URL del esquema
    
    info_container = InfoContainer(schema_sera_sustituido=mySchema)
    
    # Leer archivo CSV

    with open(entrada_csv, newline='', encoding='utf-8') as csv_file:
        reader = csv.reader(csv_file)
        header = next(reader)  # Leer la primera fila como encabezado para futuras referencias
        ultimo_tour = None
        ultimo_day = None
        ## Procesar datos y crear instancias de Tour, Day y Place
        for fila in reader:
            tipo = fila[0] if fila[0].strip() else None
            id = fila[1] if fila[1].strip() else None
            subrecurso = fila[2] if fila[2].strip() else None
            day = fila[3] if fila[3].strip() else None
            title = fila[4] if fila[4].strip() else None
            htmlDescription = fila[5] if fila[5].strip() else None
            htmlContent = fila[6] if fila[6].strip() else None
            externalUrl = fila[7] if fila[7].strip() else None
            contactCity = fila[8] if fila[8].strip() else None
            contactProvince = fila[9] if fila[9].strip() else None
            locationLat = fila[10] if fila[10].strip() else None
            locationLon = fila[11] if fila[11].strip() else None
            # Verificar el tipo de fila y crear instancias correspondientes
            match tipo:
                case 'TOUR':
                    # Instanciar Tour
                    nuevo_tour = Tour(
                        externalID=id,
                        title_es=title,
                        htmlDescription_es=htmlDescription,
                        htmlContent_es=htmlContent)
                    # Añadir Tour a la lista interna de InfoContainter
                    info_container.add_tour(nuevo_tour)
                    ultimo_tour = nuevo_tour
                case 'DAY':
                    # Instanciar Day
                    if ultimo_tour is None:
                        raise ValueError("No se puede crear un Day sin un Tour previo.")
                    nuevo_day = Day(
                        day="D"+day,
                        title_es=title,
                        htmlDescription_es=htmlDescription,
                        htmlContent_es=htmlContent)
                    # Añadir Day a la lista interna del Tour actual
                    ultimo_tour.add_day(nuevo_day)
                    ultimo_day = nuevo_day
                case 'PLACE':
                    # Instanciar Place
                    if ultimo_day is None:
                        raise ValueError("No se puede crear un Place sin un Day previo.")
                    nuevo_place = Place(
                        externalID=id,
                        submap=subrecurso,
                        title_es=title,
                        htmlDescription_es=htmlDescription,
                        htmlContent_es=htmlContent,
                        locationLat=locationLat,
                        locationLon=locationLon)
                    # Añadir Place a la lista interna del Day actual
                    ultimo_day.add_place(nuevo_place)
    return info_container

# Limpia los campos nulos de un objeto para omitirlos del JSON
def limpiar_null(obj):
    if isinstance(obj, list):
        return [limpiar_null(i) for i in obj]
    elif is_dataclass(obj):
        return {k: limpiar_null(v) for k, v in asdict(obj).items() if v is not None}
    elif isinstance(obj, dict):
        return {k: limpiar_null(v) for k, v in obj.items() if v is not None}
    else:
        return obj

def exportar_json(info_container, salida_json):
    # Elimina los campos nulos de la instacias contenidas en InfoContainer
    limpio = limpiar_null(info_container)
    # Sustituye el nombre del atributo 'schema' por '$schema'
    resultado = reemplazar_schema(limpio)

    # Aseguramos que el archivo de salida tenga la extensión .json
    if not salida_json.endswith('.json'):
        salida_json += '.json'

    # Verificar si el directorio de salida existe, si no, crearlo
    Path(salida_json).parent.mkdir(parents=True, exist_ok=True)

    # Convertir InfoContainer a JSON
    with open(salida_json, 'w', encoding='utf-8') as json_file:
        json.dump(resultado, json_file, ensure_ascii=True, indent=4)

    print(f"Archivo exportado a {salida_json} con éxito.")

def reemplazar_schema(data):
    if isinstance(data, dict):
        if 'schema_sera_sustituido' in data:
            data['$schema'] = data.pop('schema_sera_sustituido')
        for key, value in data.items():
            data[key] = reemplazar_schema(value)
    elif isinstance(data, list):
        for i in range(len(data)):
            data[i] = reemplazar_schema(data[i])
    return data
if __name__ == "__main__":
    main()





