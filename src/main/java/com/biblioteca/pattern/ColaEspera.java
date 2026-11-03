package com.biblioteca.pattern;

import java.util.LinkedList;
import java.util.Queue; //estrcutura logica
import java.util.logging.Level;
import java.util.logging.Logger;
// Clase genérica para gestionar la fila de espera (cola) de usuarios o elementos bajo el principio FIFO (First In, First Out).

public class ColaEspera<T> {
    private static final Logger LOGGER = Logger.getLogger(ColaEspera.class.getName());
    private final Queue<T> cola;

    public ColaEspera() {
        this.cola = new LinkedList<>();
    }

    public void encolar(T elemento) {
        if (elemento == null) {
            LOGGER.log(Level.WARNING, "Se intentó encolar un elemento nulo");
            return;
        }
        cola.offer(elemento);
        LOGGER.log(Level.INFO, "Elemento agregado a la cola de espera.");
    }
//Agrega un elemento al final de la cola de espera.

    public T atenderSiguiente() {
        T atendido = cola.poll();
        if (atendido != null) {
            LOGGER.log(Level.INFO, "Elemento atendido y retirado de la cola de espera.");
        } else {
            LOGGER.log(Level.WARNING, "Se intentó atender de una cola vacía.");
        }
        return atendido;
    }
    //Saca y retorna el primer elemento que está en la fila de espera.

    public T verSiguiente() {
        return cola.peek();
    } //Consulta quién es el próximo en la fila sin sacarlo de la cola.

    public int obtenerTamano() {
        return cola.size();
    } // devuelve cuantas peronas estan en la cola

    public boolean estaVacia() {
        return cola.isEmpty();
    } //verifica si la cola esta vacia

}
