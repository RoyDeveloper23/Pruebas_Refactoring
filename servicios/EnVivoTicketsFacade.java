package servicios;

import common.EventoFuncion;
import domain.Asiento;
import domain.Compra;
import domain.Espectaculo;
import domain.Funcion;
import domain.Usuario;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EnVivoTicketsFacade {
    private final Map<String, Espectaculo> espectaculos;
    private final CentroNotificacionesFuncion notificaciones;

    public EnVivoTicketsFacade(CentroNotificacionesFuncion notificaciones) {
        this.espectaculos = new HashMap<>();
        this.notificaciones = Objects.requireNonNull(notificaciones);
    }

    public void suscribirNotificaciones(ports.ObservadorFuncion observador) {
        notificaciones.suscribir(observador);
    }

    public Espectaculo registrarEspectaculo(CreadorEspectaculo creador, String nombre) {
        Objects.requireNonNull(creador);
        Espectaculo e = creador.crear(nombre);
        espectaculos.put(e.getNombre().toLowerCase(), e);
        return e;
    }

    public Funcion crearFuncion(String nombreEspectaculo, LocalDateTime fechaHora, List<Asiento> asientos) {
        Espectaculo e = buscarEspectaculo(nombreEspectaculo);
        Funcion f = new Funcion(e.getNombre(), fechaHora, asientos, notificaciones);
        e.agregarFuncion(f);

        notificaciones.avisar(new EventoFuncion(
                "NuevaFuncion",
                "Se agrego una nueva funcion",
                e.getNombre(),
                fechaHora
        ));

        return f;
    }

    public List<Funcion> verFunciones(String nombreEspectaculo) {
        return new ArrayList<>(buscarEspectaculo(nombreEspectaculo).getFunciones());
    }

    public List<Asiento> verDisponibilidad(Funcion funcion) {
        Objects.requireNonNull(funcion);
        return funcion.asientosDisponibles();
    }

    public List<Asiento> reservarAsientos(Funcion funcion, List<String> codigosAsiento, int minutosRetencion) {
        Objects.requireNonNull(funcion);
        return funcion.reservar(codigosAsiento, Duration.ofMinutes(minutosRetencion));
    }

    public Compra comprar(Usuario usuario, Funcion funcion, List<String> codigosAsiento) {
        Objects.requireNonNull(funcion);
        return funcion.confirmarCompra(usuario, codigosAsiento);
    }

    private Espectaculo buscarEspectaculo(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre del espectaculo no puede ser nulo");
        }
        Espectaculo e = espectaculos.get(nombre.toLowerCase());
        if (e == null) {
            throw new IllegalArgumentException("No existe el espectaculo: " + nombre);
        }
        return e;
    }
}
