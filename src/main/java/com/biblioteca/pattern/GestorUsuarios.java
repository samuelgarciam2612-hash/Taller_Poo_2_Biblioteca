package com.biblioteca.pattern;
import java.util.ArrayList;
import java.util.List;

//clase de la lista q añade

public class GestorUsuarios {

        private List<Observador> usuarios = new ArrayList<>();

    public void agregarObservador(Observador o) {
        usuarios.add(o);
    }
    public void eliminarObservador(Observador o) {
        usuarios.remove(o);
    }

    //todo esto es del usuario q es notificado por el sistema no del material

    public void notificar(String mensaje) {
        if (usuarios.isEmpty()) {
            return; //si no hay nada termina y si si continua
        }
        Observador primero = usuarios.remove(0); // saca al primero de la fila
        primero.estaDisponible(mensaje);
    }

    public void MaterialDisponible(String titulo) {
        notificar("Material disponible en estante: " + titulo);
    }
}
