package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import com.biblioteca.service.LibroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class LibroControllerTest {

    private LibroService libroService;
    private LibroController controller;

    @BeforeEach
    void setUp() {
        libroService = mock(LibroService.class);
        controller = new LibroController(libroService);
    }

    @Test
    void crearDelegaEnElServicio() {
        Libro libro = new Libro("El Aleph", "Jorge Luis Borges");
        when(libroService.crearLibro("El Aleph", "Jorge Luis Borges")).thenReturn(libro);

        assertSame(libro, controller.crear("El Aleph", "Jorge Luis Borges"));
    }

    @Test
    void listarDelegaEnElServicio() {
        List<Libro> libros = List.of(new Libro());
        when(libroService.listarLibros()).thenReturn(libros);

        assertSame(libros, controller.listar());
    }

    @Test
    void buscarDelegaEnElServicio() {
        Libro libro = new Libro();
        when(libroService.buscarLibro(1L)).thenReturn(libro);

        assertSame(libro, controller.buscar(1L));
    }

    @Test
    void eliminarDelegaEnElServicio() {
        controller.eliminar(1L);

        verify(libroService).eliminarLibro(1L);
    }
}
