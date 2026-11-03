package com.biblioteca.util;

import java.util.List;
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
    public static <T extends Comparable<? super T>> void ordenar(List<T> lista) {
        if (lista == null || lista.size() <= 1) {
            return;
        }

        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compara elementos adyacentes usando compareTo
                if (lista.get(j).compareTo(lista.get(j + 1)) > 0) {
                    // Intercambio de posiciones
                    T temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
    }
    //algoritmo de ordenamiento burbuja
}
