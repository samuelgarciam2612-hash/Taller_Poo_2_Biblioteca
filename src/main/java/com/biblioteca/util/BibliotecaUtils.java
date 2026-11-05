package com.biblioteca.util;

import com.biblioteca.model.Material;

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
                LOGGER.log(Level.WARNING, "Validación fallida: El campo es nulo o está en blanco.", nombreCampo);
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

    public static <T> List<T> filtrar(List<T> lista, java.util.function.Predicate<T> criterio) {
        if (lista == null || criterio == null) {
            return List.of();
        }
        return lista.stream()
                .filter(criterio)
                .toList();
    }
    // busqueda o filtrado con predicado

    // Obtener el primer elemento o valor por defecto
    public static <T> T obtenerPrimeroODefecto(List<T> lista, T valorPorDefecto) {
        if (lista == null || lista.isEmpty()) {
            return valorPorDefecto;
        }
        return lista.getFirst();
    }

    // Comodín no acotado <?>: solo lee o cuenta sin importar el tipo
    public static int contarElementos(List<?> lista) {
        if (lista == null) {
            return 0;
        }
        return lista.size();
    }

    // Productor (? extends T): lee y procesa una colección de subtipos (ej. Libro, Revista)
    public static double calcularMultaTotal(List<? extends Material> materiales, int diasRetraso) {
        if (materiales == null || diasRetraso <= 0) {
            return 0.0;
        }
        double total = 0.0;
        for (Material m : materiales) {
            total += m.calcularMulta(diasRetraso);
        }
        return total;
    }

    // Consumidor (? super T): agrega o deposita elementos en una colección de supertipos
    public static <T> void transferirElementos(List<T> origen, List<? super T> destino) {
        if (origen == null || destino == null) {
            return;
        }
        destino.addAll(origen);
    }
}

