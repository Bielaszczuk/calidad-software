package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BibliotecaServiceTest {

    private LibroRepository libroRepository;
    private UsuarioRepository usuarioRepository;
    private PrestamoRepository prestamoRepository;
    private BibliotecaService service;

    @BeforeEach
    void setUp() {
        libroRepository = mock(LibroRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        prestamoRepository = mock(PrestamoRepository.class);
        service = new BibliotecaService(libroRepository, usuarioRepository, prestamoRepository);
    }

    @Test
    void noHaceNadaCuandoElUsuarioNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        service.prestarLibro(2L, 1L);

        verifyNoInteractions(libroRepository, prestamoRepository);
    }

    @Test
    void noHaceNadaCuandoElUsuarioEstaInactivo() {
        Usuario usuario = usuarioActivo(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        service.prestarLibro(2L, 1L);

        verifyNoInteractions(libroRepository, prestamoRepository);
    }

    @Test
    void noHaceNadaCuandoElLibroNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioActivo(true)));
        when(libroRepository.findById(2L)).thenReturn(Optional.empty());

        service.prestarLibro(2L, 1L);

        verify(libroRepository, never()).save(any());
        verifyNoInteractions(prestamoRepository);
    }

    @Test
    void noHaceNadaCuandoElLibroYaEstaPrestado() {
        Libro libro = new Libro();
        libro.setPrestado(true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioActivo(true)));
        when(libroRepository.findById(2L)).thenReturn(Optional.of(libro));

        service.prestarLibro(2L, 1L);

        verify(libroRepository, never()).save(any());
        verifyNoInteractions(prestamoRepository);
    }

    @Test
    void prestaElLibroYGuardaElPrestamo() {
        Usuario usuario = usuarioActivo(true);
        Libro libro = new Libro("El Aleph", "Jorge Luis Borges");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(libroRepository.findById(2L)).thenReturn(Optional.of(libro));

        service.prestarLibro(2L, 1L);

        assertTrue(libro.isPrestado());
        verify(libroRepository).save(libro);
        ArgumentCaptor<Prestamo> captor = ArgumentCaptor.forClass(Prestamo.class);
        verify(prestamoRepository).save(captor.capture());
        assertSame(libro, captor.getValue().getLibro());
        assertSame(usuario, captor.getValue().getUsuario());
    }

    private Usuario usuarioActivo(boolean activo) {
        Usuario usuario = new Usuario();
        usuario.setActivo(activo);
        return usuario;
    }
}
