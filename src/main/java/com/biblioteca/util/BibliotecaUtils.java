package com.biblioteca.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BibliotecaUtils {
    private static final Logger LOGGER = Logger.getLogger(BibliotecaUtils.class.getName());
    // Constructor privado para evitar que se creen instancias de esta clase utilitaria
    private BibliotecaUtils() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no puede ser instanciada");
    }

        public static boolean esTextoValido(String texto, String nombreCampo) {
            if (texto == null || texto.isBlank()) {
                LOGGER.log(Level.WARNING, "Validación fallida: El campo ''{0}'' es nulo o está en blanco.", nombreCampo);
                return false;
            }
            return true;
        }//Valida si un texto es nulo o está en blanco

        public static String limpiarTexto(String texto) {
            if (texto == null) {
                return "";
            }
            return texto.trim();
    }
    //Limpia un texto eliminando espacios sobrantes al inicio y al final.
}
