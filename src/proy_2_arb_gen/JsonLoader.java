/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonLoader {
    public String load(String nombreArchivo) {
        if (nombreArchivo == null) {
            return null;
        }
        if (nombreArchivo == "") {
            return null;
        }
        if (!nombreArchivo.endsWith(".json")) {
            return null;
        }
        Path archivo = Paths.get(nombreArchivo);
        String json = null;
        try {
            json = new String(Files.readAllBytes(archivo));
        } catch (Exception e) {
            return null;
        }
        return json;
    }
}
