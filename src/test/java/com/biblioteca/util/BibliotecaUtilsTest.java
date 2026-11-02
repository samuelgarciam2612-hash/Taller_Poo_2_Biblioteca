package com.biblioteca.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaUtilsTest {
    @Test
    void testEsTextoValidoConTextoCorrecto() {
        String texto = "Programación Java";
        assertTrue(BibliotecaUtils.esTextoValido(texto, "Título"),
                "El texto válido debería retornar true");
    }// Aquí probamos que si le pasamos un texto normal y bien escrito el método acepte y devuelva true porque sí sirve.

    @Test
    void testEsTextoValidoConNuloOVacio() {
        assertFalse(BibliotecaUtils.esTextoValido(null, "CampoNulo"),
                "Un texto nulo debe retornar false");
        assertFalse(BibliotecaUtils.esTextoValido("   ", "CampoEspacios"),
                "Un texto solo con espacios debe retornar false");
    }// Aquí probamos que el filtro funcione y rechace las trampas. Si le mandamos un valor nulo o un espacio en blanco, debe retornar false.

    @Test
    void testLimpiarTexto() {
        String textoConEspacios = "   Libro de POO   ";

        String resultado = BibliotecaUtils.limpiarTexto(textoConEspacios);

        assertEquals("Libro de POO", resultado,
                "Debe eliminar los espacios sobrantes al inicio y al final");
    }
    // Aquí probamos que si a un texto se le escapan espacios molestos a los lados ("   Libro  "), el método limpie todo y nos devuelva el texto limpio y derecho ("Libro").

    @Test
    void testLimpiarTextoNulo() {
        String resultado = BibliotecaUtils.limpiarTexto(null);
        assertEquals("", resultado, "Si el texto es nulo, debe retornar una cadena vacía");
    }
    // Aquí probamos la seguridad: si por accidente a la función de limpiar le llega un nulo, no se nos va a caer el programa con un error rojo, sino que nos devolverá un texto vacío ("") de forma segura.
    /*
     * RESULTADO ESPERADO AL EJECUTAR:
     * - Deben pasar los 4 tests en verde sin problemas.
     * - Sale un WARNING en rojo en la consola porque probamos a propósito
     *   mandar datos nulos y vacíos, y el Logger debe avisar de eso. No es un error.
     */
}