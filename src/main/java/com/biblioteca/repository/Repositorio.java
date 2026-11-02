package com.biblioteca.repository;

import java.util.List;
import java.util.Optional;

public interface Repositorio <T>{

    void guardar(T elemento);
    // Recibe un elemento de cualquier tipo (Libro, Usuario, etc.) y lo guarda en la colección.

    Optional<T> buscarPorId(String id);
    // Busca un elemento por su ID. Devuelve un Optional para manejar de forma segura el caso en que no se encuentre y evitar errores de null.
    List<T> listarTodos();
    // Devuelve una lista con todos los elementos que están guardados actualmente en el repositorio.
    boolean eliminar(String id);
    // Elimina un elemento usando su ID. Retorna true si lo encontró y lo borró, o false si no existía.
}
