package com.biblioteca.util;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Material;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

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


    @Test
    void testFiltrarElementosCorrectamente() {
        List<String> nombres = List.of("Cálculo", "Física", "Química", "Filosofía");

        // Filtramos solo las palabras que empiezan por la letra "F"
        List<String> filtrados = BibliotecaUtils.filtrar(nombres, n -> n.startsWith("F"));

        assertEquals(2, filtrados.size(), "Debió encontrar exactamente 2 elementos que empiezan por F");
        assertTrue(filtrados.contains("Física"));
        assertTrue(filtrados.contains("Filosofía"));
    }
    // Probamos que el método genérico aplique la lambda correctamente y devuelva solo los elementos que cumplen la condición.

    @Test
    void testFiltrarConListaNulaOCriterioNulo() {
        List<String> nombres = List.of("Java", "Python");

        List<String> resultadoListaNula = BibliotecaUtils.filtrar(null, n -> n.length() > 2);
        List<String> resultadoCriterioNulo = BibliotecaUtils.filtrar(nombres, null);

        assertTrue(resultadoListaNula.isEmpty(), "Si la lista de entrada es nula debe retornar una lista vacía sin caerse");
        assertTrue(resultadoCriterioNulo.isEmpty(), "Si el criterio de búsqueda es nulo debe retornar una lista vacía");
    }
    // Validamos la tolerancia a fallos: pasarle nulos a filtrar no arroja NullPointerException, sino una lista vacía segura.

    @Test
    void testObtenerPrimeroODefectoConListaConElementos() {
        List<Integer> numeros = List.of(10, 20, 30);

        Integer primero = BibliotecaUtils.obtenerPrimeroODefecto(numeros, -1);

        assertEquals(10, primero, "Debe retornar el primer elemento de la lista (10)");
    }
    // Probamos el caso exitoso: si la lista tiene datos, extrae el primero sin alterar el flujo.

    @Test
    void testObtenerPrimeroODefectoConListaVaciaONula() {
        List<String> listaVacia = Collections.emptyList();

        String valorDefectoVacia = BibliotecaUtils.obtenerPrimeroODefecto(listaVacia, "VALOR_DEFECTO");
        String valorDefectoNula = BibliotecaUtils.obtenerPrimeroODefecto(null, "VALOR_DEFECTO");

        assertEquals("VALOR_DEFECTO", valorDefectoVacia, "En una lista vacía debe retornar el valor por defecto provisto");
        assertEquals("VALOR_DEFECTO", valorDefectoNula, "Si la lista es nula debe retornar el valor por defecto de manera segura");
    }
    // Comprobamos que al estar vacía o nula, no salte NoSuchElementException con getFirst(), sino que rescate el valor por defecto configurado.
    @Test
    void testContarElementosWildcard() {
        List<String> palabras = List.of("A", "B", "C");
        List<Integer> numeros = List.of(1, 2, 3, 4);

        assertEquals(3, BibliotecaUtils.contarElementos(palabras));
        assertEquals(4, BibliotecaUtils.contarElementos(numeros));
        assertEquals(0, BibliotecaUtils.contarElementos(null));
    }

    @Test
    void testCalcularMultaTotalProductorExtends() {
        // Probamos con una lista del subtipo específico Libro
        List<Libro> libros = List.of(
                new Libro("L1", "Libro A", "Autor A", 2020),
                new Libro("L2", "Libro B", "Autor B", 2021)
        );

        // Si cada libro tiene tarifa de multa (ej. $1000/día), 2 días de retraso deben procesarse sin error de tipado
        double total = BibliotecaUtils.calcularMultaTotal(libros, 2);
        assertTrue(total >= 0.0);
    }

    @Test
    void testTransferirElementosConsumidorSuper() {
        List<Libro> listaOrigen = List.of(new Libro("L1", "Libro 1", "Autor", 2020));
        List<Material> listaDestino = new java.util.ArrayList<>();

        // Se transfieren subtipos (Libro) a una colección de su supertipo (Material)
        BibliotecaUtils.transferirElementos(listaOrigen, listaDestino);

        assertEquals(1, listaDestino.size());
        assertEquals("L1", listaDestino.getFirst().getId());
    }

}