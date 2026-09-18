package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibroServiceTest {

    @Test
    void crearLibro2ReutilizaLaCreacionPrincipal() {
        LibroRepository repository = mock(LibroRepository.class);
        when(repository.save(any(Libro.class))).thenAnswer(invocacion -> invocacion.getArgument(0, Libro.class));
        LibroService service = new LibroService(repository);

        Libro libro = service.crearLibro2("El Aleph", "Jorge Luis Borges");

        assertEquals("El Aleph", libro.getTitulo());
        verify(repository).save(any(Libro.class));
    }
}
