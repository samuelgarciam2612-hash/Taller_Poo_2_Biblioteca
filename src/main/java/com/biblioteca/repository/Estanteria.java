package com.biblioteca.repository;

import com.biblioteca.model.Identificable;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Estanteria <T extends Identificable> implements Repositorio<T>{
    // Clase genérica restringida a elementos Identificables que implementa las operaciones del repositorio en memoria.

    private static final Logger LOGGER = Logger.getLogger(Estanteria.class.getName());
    private final List<T> elementos;
// Logger para registrar eventos y lista interna para almacenar los elementos en memoria.

    public Estanteria() {
        this.elementos = new ArrayList<>();
    }

    @Override
    public void guardar(T elemento) {
        Objects.requireNonNull(elemento, "No se puede guardar un elemento nulo en la estantería");
        elementos.add(elemento);
        LOGGER.log(Level.INFO, "Elemento guardado con ID: {0}", elemento.getId());
    }
    // Valida que el objeto no sea nulo, lo agrega a la lista y registra la acción en el log.

    @Override
    public Optional<T> buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            LOGGER.log(Level.WARNING, "Se intentó buscar con un ID nulo o vacío");
            return Optional.empty();
        }
        for (T elemento : elementos) {
            if (id.equals(elemento.getId())) {
                return Optional.of(elemento);
            }
        }
        return Optional.empty();
    }
    // Recorre la estantería buscando coincidencia de ID y retorna un Optional con el elemento si lo halla.


    @Override
    public List<T> listarTodos() {
        return Collections.unmodifiableList(new ArrayList<>(elementos));
    }
    // Retorna una vista de solo lectura con todos los elementos guardados para proteger la integridad de la estantería.


    @Override
    public boolean eliminar(String id) {
        Optional<T> encontrado = buscarPorId(id);
        if (encontrado.isPresent()) {
            elementos.remove(encontrado.get());
            LOGGER.log(Level.INFO, "Elemento eliminado con ID: {0}", id);
            return true;
        }
        LOGGER.log(Level.WARNING, "No se encontró el elemento con ID {0} para eliminar", id);
        return false;
    }
    // Busca el elemento por su ID, lo elimina si existe retornando true, o retorna false si no se encuentra.

    public int cantidadElementos() {
        return elementos.size();
    }
}
