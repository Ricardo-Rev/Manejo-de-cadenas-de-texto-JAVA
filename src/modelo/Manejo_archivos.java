/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.io.*; // Importa todas las clases necesarias para operaciones de archivo
/**
 *
 * @author DELL
 */
public class Manejo_archivos {

    public String leerArchivo(File archivo) throws IOException {
        StringBuilder contenido = new StringBuilder(); // Crea un StringBuilder para acumular el contenido del archivo
        
        // Usa un bloque try-with-resources para asegurarse de que BufferedReader se cierre automáticamente
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea; // Variable para almacenar cada línea del archivo
            
            // Lee cada línea del archivo hasta que no haya más líneas
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n"); // Añade la línea al StringBuilder y agrega un salto de línea
            }
        }
        // Devuelve el contenido del archivo como una cadena
        return contenido.toString();
    }


    public void escribirArchivo(File archivo, String contenido) throws IOException {
        // Usa un bloque try-with-resources para asegurarse de que BufferedWriter se cierre automáticamente
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write(contenido); // Escribe el contenido en el archivo
        }
    }
}
