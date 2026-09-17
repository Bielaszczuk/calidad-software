package com.biblioteca.steps;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.service.PrestamoService;
import io.cucumber.java.Before;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class PrestamoServiceSteps {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    private Exception excepcionCapturada;
    private Prestamo prestamoCreado;
    private double resultadoCalculo;
    private List<Prestamo> listaPrestamos;

    public PrestamoServiceSteps() {
        MockitoAnnotations.openMocks(this);
    }

    @Before
    public void prepararEscenario() {
        reset(usuarioRepository, libroRepository, prestamoRepository);
        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0, Prestamo.class));
        when(prestamoRepository.findAll()).thenReturn(List.of());
        excepcionCapturada = null;
        prestamoCreado = null;
        listaPrestamos = null;
    }

    @Dado("que existe un usuario activo de id {long}")
    public void crearUsuarioActivo(Long id) {
        registrarUsuario(id, true, false, 0);
    }

    @Dado("que existe un usuario inactivo de id {long}")
    public void crearUsuarioInactivo(Long id) {
        registrarUsuario(id, false, false, 0);
    }

    @Dado("que existe un usuario moroso de id {long}")
    public void crearUsuarioMoroso(Long id) {
        registrarUsuario(id, true, true, 0);
    }

    @Dado("que existe un usuario activo con tres prestamos de id {long}")
    public void crearUsuarioConMaximoPrestamos(Long id) {
        registrarUsuario(id, true, false, 3);
    }

    @Dado("que existe un libro disponible de id {long}")
    public void crearLibroDisponible(Long id) {
        registrarLibro(id, false);
    }

    @Dado("que existe un libro ya prestado de id {long}")
    public void crearLibroPrestado(Long id) {
        registrarLibro(id, true);
    }

    @Dado("que existe un préstamo activo de id {long}")
    public void crearPrestamoActivo(Long id) {
        registrarPrestamo(id, false);
    }

    @Dado("que existe un préstamo devuelto de id {long}")
    public void crearPrestamoDevuelto(Long id) {
        registrarPrestamo(id, true);
    }

    @Cuando("intento prestar un libro con usuario id {long} y libro id {long}")
    public void intentoPrestarLibro(Long usuarioId, Long libroId) {
        capturarExcepcion(() -> prestamoService.prestarLibro(usuarioId, libroId));
    }

    @Cuando("presto el libro con usuario id {long} y libro id {long}")
    public void prestarLibroExito(Long usuarioId, Long libroId) {
        prestamoCreado = prestamoService.prestarLibro(usuarioId, libroId);
    }

    @Cuando("calculo el recargo A para un préstamo nulo")
    public void calcularRecargoANulo() {
        resultadoCalculo = prestamoService.calcularRecargoA(null);
    }

    @Cuando("calculo el recargo A para un préstamo de hace {int} días")
    public void calcularRecargoAConDias(int dias) {
        resultadoCalculo = prestamoService.calcularRecargoA(prestamoDeHace(dias));
    }

    @Cuando("calculo el recargo B para un préstamo nulo")
    public void calcularRecargoBNulo() {
        resultadoCalculo = prestamoService.calcularRecargoB(null);
    }

    @Cuando("calculo el recargo B para un préstamo de hace {int} días")
    public void calcularRecargoBConDias(int dias) {
        resultadoCalculo = prestamoService.calcularRecargoB(prestamoDeHace(dias));
    }

    @Cuando("intento devolver el préstamo con id {long}")
    public void intentoDevolverLibro(Long id) {
        capturarExcepcion(() -> prestamoService.devolverLibro(id));
    }

    @Cuando("devuelvo el préstamo con id {long}")
    public void devolverLibroExito(Long id) {
        prestamoService.devolverLibro(id);
    }

    @Cuando("intento devolver un préstamo antiguo con id {long}")
    public void intentoDevolverLibroAntiguo(Long id) {
        capturarExcepcion(() -> prestamoService.devolverLibroAntiguo(id));
    }

    @Cuando("devuelvo el préstamo antiguo con id {long}")
    public void devolverLibroAntiguoExito(Long id) {
        prestamoService.devolverLibroAntiguo(id);
    }

    @Cuando("calculo la multa para un préstamo nulo")
    public void calcularMultaNulo() {
        resultadoCalculo = prestamoService.calcularMulta(null);
    }

    @Cuando("calculo la multa no devuelto de hace {int} días")
    public void calcularMultaNoDevuelto(int dias) {
        Prestamo prestamo = prestamoDeHace(dias);
        prestamo.setDevuelto(false);
        resultadoCalculo = prestamoService.calcularMulta(prestamo);
    }

    @Cuando("calculo la multa devuelto con diferencia de {int} días")
    public void calcularMultaDevuelto(int dias) {
        Prestamo prestamo = prestamoDeHace(dias);
        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucion(LocalDate.now(ZoneId.systemDefault()));
        resultadoCalculo = prestamoService.calcularMulta(prestamo);
    }

    @Cuando("solicito la lista de todos los préstamos")
    public void listarPrestamos() {
        listaPrestamos = prestamoService.listarPrestamos();
    }

    @Entonces("se lanza una excepción en prestamo con mensaje {string}")
    public void verificarExcepcion(String mensajeEsperado) {
        assertNotNull(excepcionCapturada, "Se esperaba una excepción pero no ocurrió.");
        assertEquals(mensajeEsperado, excepcionCapturada.getMessage());
    }

    @Entonces("el préstamo creado no es nulo y el libro queda prestado")
    public void verificarPrestamoCreado() {
        assertNotNull(prestamoCreado);
        assertTrue(prestamoCreado.getLibro().isPrestado());
    }

    @Entonces("^el recargo A devuelto es ([0-9]+(?:\\.[0-9]+)?)$")
    public void verificarRecargoA(String esperado) {
        assertEquals(Double.parseDouble(esperado), resultadoCalculo, 0.01);
    }

    @Entonces("^el recargo B devuelto es ([0-9]+(?:\\.[0-9]+)?)$")
    public void verificarRecargoB(String esperado) {
        assertEquals(Double.parseDouble(esperado), resultadoCalculo, 0.01);
    }

    @Entonces("la devolución finaliza correctamente")
    public void verificarDevolucionExito() {
        assertNull(excepcionCapturada);
    }

    @Entonces("^la multa devuelta es ([0-9]+(?:\\.[0-9]+)?)$")
    public void verificarMulta(String esperado) {
        assertEquals(Double.parseDouble(esperado), resultadoCalculo, 0.01);
    }

    @Entonces("la lista de préstamos devuelta no es nula")
    public void verificarListaPrestamos() {
        assertNotNull(listaPrestamos);
    }

    private void registrarUsuario(Long id, boolean activo, boolean moroso, int cantidadPrestamos) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setActivo(activo);
        usuario.setMoroso(moroso);
        List<Prestamo> prestamos = new ArrayList<>();
        for (int i = 0; i < cantidadPrestamos; i++) {
            prestamos.add(new Prestamo());
        }
        usuario.setPrestamos(prestamos);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
    }

    private void registrarLibro(Long id, boolean prestado) {
        Libro libro = new Libro();
        libro.setId(id);
        libro.setPrestado(prestado);
        when(libroRepository.findById(id)).thenReturn(Optional.of(libro));
    }

    private void registrarPrestamo(Long id, boolean devuelto) {
        Libro libro = new Libro();
        libro.setPrestado(!devuelto);
        Prestamo prestamo = new Prestamo();
        prestamo.setId(id);
        prestamo.setDevuelto(devuelto);
        prestamo.setLibro(libro);
        when(prestamoRepository.findById(id)).thenReturn(Optional.of(prestamo));
    }

    private Prestamo prestamoDeHace(int dias) {
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(LocalDate.now(ZoneId.systemDefault()).minusDays(dias));
        return prestamo;
    }

    private void capturarExcepcion(Runnable accion) {
        try {
            accion.run();
        } catch (Exception e) {
            excepcionCapturada = e;
        }
    }
}
