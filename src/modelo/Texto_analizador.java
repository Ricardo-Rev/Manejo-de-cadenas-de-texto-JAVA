/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.util.HashMap;  // Importa la clase HashMap para almacenar el conteo de vocales
import java.util.Map;     // Importa la interfaz Map que HashMap implementa
import java.text.Normalizer;  // Importa la clase Normalizer para normalizar el texto y eliminar diacríticos
/**
 *
 * @author DELL
 */
public class Texto_analizador {
    private String texto;  // Variable para almacenar el texto

    // Constructor para inicializar la clase con un texto
    public Texto_analizador(String texto) {
        this.texto = texto;
    }

    // Obtener la longitud total del texto
    public int obtenerLongitud() {
        return texto.length();  // Retorna la longitud del texto
    }

    // Contar el número de palabras en el texto
    public int contarPalabras() {
        // Dividir el texto en palabras usando el espacio como delimitador
        String[] palabras = texto.trim().split("\\s+");
        return palabras.length;  // Retorna la cantidad de palabras
    }

    // Obtener la primera letra del texto
    public String obtenerPrimeraLetra() {
        // Retorna la primera letra si el texto no es nulo, no está vacío, y no solo tiene espacios
        return (texto != null && !texto.trim().isEmpty()) ? 
            String.valueOf(texto.trim().charAt(0)) : "";  // Retorna la primera letra o cadena vacía
    }

    // Obtener la última letra del texto
    public String obtenerUltimaLetra() {
        // Retorna la última letra si el texto no es nulo, no está vacío, y no solo tiene espacios
        return (texto != null && !texto.trim().isEmpty()) ? 
            String.valueOf(texto.trim().charAt(texto.trim().length() - 1)) : "";  // Retorna la última letra o cadena vacía
    }

    // Obtener la letra central del texto
    public String obtenerLetraCentral() {
        // Verificar que el texto no sea nulo o vacío
        if (texto == null || texto.trim().isEmpty()) {
            return "";  // Retorna una cadena vacía si el texto es nulo o vacío
        }

        // Obtener la longitud del texto sin espacios al inicio y al final
        int longitud = texto.trim().length();

        // Calcular la posición central
        int posicionCentral = longitud / 2;

        // Retorna el carácter en la posición central del texto
        return String.valueOf(texto.trim().charAt(posicionCentral));
    }

    // Obtener la primera palabra del texto
    public String obtenerPrimeraPalabra() {
        // Dividir el texto en palabras y retornar la primera palabra si existe
        String[] palabras = texto.trim().split("\\s+");
        return palabras.length > 0 ? palabras[0] : "";  // Retorna la primera palabra o cadena vacía
    }

    // Obtener la palabra central del texto
    public String obtenerPalabraCentral() {
        // Dividir el texto en palabras
        String[] palabras = texto.trim().split("\\s+");
        
        // Retorna una cadena vacía si no hay palabras
        if (palabras.length == 0) return "";
        
        // Calcular el índice de la palabra central
        int indiceCentral = palabras.length / 2;
        
        // Si el número de palabras es par, retorna la palabra central izquierda, si es impar, retorna la palabra central
        return palabras.length % 2 == 0 ? palabras[indiceCentral - 1] : palabras[indiceCentral];
    }

    // Obtener la última palabra del texto
    public String obtenerUltimaPalabra() {
        // Dividir el texto en palabras y retornar la última palabra si existe
        String[] palabras = texto.trim().split("\\s+");
        return palabras.length > 0 ? palabras[palabras.length - 1] : "";  // Retorna la última palabra o cadena vacía
    }

    // Contar el total de todas las vocales (mayúsculas, minúsculas y con tilde)
    public Map<Character, Integer> contarRepeticionesVocales() {
        Map<Character, Integer> conteoVocales = new HashMap<>();
        
        // Normalizar el texto para eliminar tildes y diacríticos
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        textoNormalizado = textoNormalizado.replaceAll("\\p{M}", "");

        // Definir las vocales sin tilde y con tilde
        String vocales = "aeiou";
        String vocalesConTilde = "áéíóú";

        // Inicializa el mapa con las vocales sin tilde
        for (char vocal : vocales.toCharArray()) {
            conteoVocales.put(vocal, contarRepeticionesVocales(vocal, textoNormalizado));
        }
        
        // Inicializa el mapa con las vocales con tilde
        for (char vocalConTilde : vocalesConTilde.toCharArray()) {
            conteoVocales.put(vocalConTilde, contarRepeticionesVocales(vocalConTilde, textoNormalizado));
        }

        return conteoVocales;  // Retorna el mapa con el conteo de vocales
    }

    // Contar repeticiones de una letra en el texto
    private int contarRepeticionesVocales(char letra, String texto) {
        int contador = 0;
        
        // Recorrer cada carácter del texto
        for (char c : texto.toCharArray()) {
            // Comparar el carácter actual con la letra en minúscula
            if (Character.toLowerCase(c) == Character.toLowerCase(letra)) {
                contador++;  // Incrementar el contador si hay coincidencia
            }
        }
        return contador;  // Retornar el conteo de repeticiones
    }

    // Contar palabras con longitud par
    public int contarPalabrasPar() {
        return contarPalabrasPorLongitud(true);  // Llamar al método auxiliar con parámetro true para longitud par
    }

    // Contar palabras con longitud impar
    public int contarPalabrasImpar() {
        return contarPalabrasPorLongitud(false);  // Llamar al método auxiliar con parámetro false para longitud impar
    }

    // Contar palabras con longitud par o impar
    private int contarPalabrasPorLongitud(boolean esPar) {
        int count = 0;
        
        // Dividir el texto en palabras
        for (String palabra : texto.trim().split("\\s+")) {
            // Contar palabras con longitud que coincide con el parámetro esPar
            if (palabra.length() % 2 == (esPar ? 0 : 1)) {
                count++;  // Incrementar el contador si la condición se cumple
            }
        }
        return count;  // Retornar el conteo de palabras
    }

    // Traducir el texto a una clave de "murciélago"
    public String traducirAMurcielago() {
        // Normalizar el texto para eliminar tildes y diacríticos
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        textoNormalizado = textoNormalizado.replaceAll("\\p{M}", "");

        // Reemplazar las letras del texto por sus valores correspondientes en la clave
        return textoNormalizado.replaceAll("(?i)m", "0")
                               .replaceAll("(?i)u", "1")
                               .replaceAll("(?i)r", "2")
                               .replaceAll("(?i)c", "3")
                               .replaceAll("(?i)i", "4")
                               .replaceAll("(?i)e", "5")
                               .replaceAll("(?i)l", "6")
                               .replaceAll("(?i)a", "7")
                               .replaceAll("(?i)g", "8")
                               .replaceAll("(?i)o", "9");
    }

    // Obtener el texto
    public String getTexto() {
        return texto;  // Retornar el texto
    }

    // Establecer el texto
    public void setTexto(String texto) {
        this.texto = texto;  // Asignar un nuevo valor al texto
    }
}
