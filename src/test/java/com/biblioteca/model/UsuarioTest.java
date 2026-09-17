package com.biblioteca.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void constructorInicializaUnUsuarioActivoYNoMoroso() {
        Usuario usuario = new Usuario("Ana", "ana@email.com");

        assertAll(
                () -> assertEquals("Ana", usuario.getNombre()),
                () -> assertEquals("ana@email.com", usuario.getEmail()),
                () -> assertTrue(usuario.isActivo()),
                () -> assertFalse(usuario.isMoroso()),
                () -> assertTrue(usuario.getPrestamos().isEmpty())
        );
    }

    @Test
    void gettersYSettersConservanLosDatos() {
        Usuario usuario = new Usuario();
        List<Prestamo> prestamos = List.of(new Prestamo());
        usuario.setId(1L);
        usuario.setNombre("Ana");
        usuario.setEmail("ana@email.com");
        usuario.setActivo(false);
        usuario.setMoroso(true);
        usuario.setPrestamos(prestamos);

        assertAll(
                () -> assertEquals(1L, usuario.getId()),
                () -> assertEquals("Ana", usuario.getNombre()),
                () -> assertEquals("ana@email.com", usuario.getEmail()),
                () -> assertFalse(usuario.isActivo()),
                () -> assertTrue(usuario.isMoroso()),
                () -> assertSame(prestamos, usuario.getPrestamos())
        );
    }
}
