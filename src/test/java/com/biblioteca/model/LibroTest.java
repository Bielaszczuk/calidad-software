package com.biblioteca.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibroTest {

    @Test
    void prestaUnLibroConDatosValidos() {
        Libro libro = new Libro("El Aleph", "Jorge Luis Borges");

        assertEquals("Libro prestado correctamente", libro.prestar());
        assertTrue(libro.isPrestado());
    }

    @Test
    void rechazaUnLibroYaPrestado() {
        Libro libro = new Libro("El Aleph", "Jorge Luis Borges");
        libro.setPrestado(true);

        assertEquals("El libro ya está prestado", libro.prestar());
    }

    @Test
    void rechazaTituloNulo() {
        assertEquals("No se puede prestar", new Libro(null, "Autor").prestar());
    }

    @Test
    void rechazaTituloEnBlanco() {
        assertEquals("No se puede prestar", new Libro("   ", "Autor").prestar());
    }

    @Test
    void rechazaAutorNulo() {
        assertEquals("No se puede prestar", new Libro("Título", null).prestar());
    }

    @Test
    void rechazaAutorEnBlanco() {
        assertEquals("No se puede prestar", new Libro("Título", "   ").prestar());
    }

    @Test
    void gettersYSettersConservanLosDatos() {
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Título");
        libro.setAutor("Autor");
        libro.setPrestado(true);

        assertAll(
                () -> assertEquals(1L, libro.getId()),
                () -> assertEquals("Título", libro.getTitulo()),
                () -> assertEquals("Autor", libro.getAutor()),
                () -> assertTrue(libro.isPrestado())
        );
    }
}
