package domain;

import common.EstadoAsiento;
import common.EventoFuncion;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import servicios.CentroNotificacionesFuncion;

public class Funcion {
    private final String nombreEspectaculo;
    private LocalDateTime fechaHora;
    private final List<Asiento> asientos;
    private final CentroNotificacionesFuncion notificaciones;

    public Funcion(String nombreEspectaculo, LocalDateTime fechaHora, List<Asiento> asientos, CentroNotificacionesFuncion notificaciones) {
        this.nombreEspectaculo = Objects.requireNonNull(nombreEspectaculo);
        this.fechaHora = Objects.requireNonNull(fechaHora);
        this.asientos = new ArrayList<>(Objects.requireNonNull(asientos));
        this.notificaciones = Objects.requireNonNull(notificaciones);
    }

    public String getNombreEspectaculo() {
        return nombreEspectaculo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    public void cambiarFecha(LocalDateTime nuevaFechaHora) {
        fechaHora = Objects.requireNonNull(nuevaFechaHora);
        notificaciones.avisar(new EventoFuncion(
                "Reprogramacion",
                "La funcion cambio de fecha/hora",
                nombreEspectaculo,
                fechaHora
        ));
    }

    public void liberarReservasVencidas() {
        Instant ahora = Instant.now();
        for (Asiento a : asientos) {
            a.estaDisponible(ahora);
        }
    }

    public List<Asiento> asientosDisponibles() {
        liberarReservasVencidas();
        Instant ahora = Instant.now();
        List<Asiento> disponibles = new ArrayList<>();
        for (Asiento a : asientos) {
            if (a.estaDisponible(ahora) && a.getEstado() == EstadoAsiento.DISPONIBLE) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    public List<Asiento> reservar(List<String> codigosAsiento, Duration duracion) {
        Objects.requireNonNull(codigosAsiento);
        if (duracion == null || duracion.isZero() || duracion.isNegative()) {
            throw new IllegalArgumentException("La duracion debe ser positiva");
        }

        liberarReservasVencidas();

        List<Asiento> seleccion = new ArrayList<>();
        for (String codigo : codigosAsiento) {
            Asiento a = buscarAsiento(codigo);
            if (a.getEstado() != EstadoAsiento.DISPONIBLE) {
                throw new IllegalStateException("El asiento " + codigo + " no esta disponible");
            }
            seleccion.add(a);
        }

        Instant hasta = Instant.now().plus(duracion);
        for (Asiento a : seleccion) {
            a.reservarHasta(hasta);
        }

        notificaciones.avisar(new EventoFuncion(
                "Reserva",
                "Se reservaron " + seleccion.size() + " asientos por " + duracion.toMinutes() + " minutos",
                nombreEspectaculo,
                fechaHora
        ));

        return seleccion;
    }

    public Compra confirmarCompra(Usuario usuario, List<String> codigosAsiento) {
        Objects.requireNonNull(usuario);
        Objects.requireNonNull(codigosAsiento);

        liberarReservasVencidas();

        List<Asiento> comprados = new ArrayList<>();
        for (String codigo : codigosAsiento) {
            Asiento a = buscarAsiento(codigo);
            if (a.getEstado() == EstadoAsiento.AGOTADO) {
                throw new IllegalStateException("El asiento " + codigo + " ya esta agotado");
            }
            if (a.getEstado() == EstadoAsiento.DISPONIBLE) {
                throw new IllegalStateException("El asiento " + codigo + " no esta reservado");
            }
            a.confirmarCompra();
            comprados.add(a);
        }

        Compra compra = new Compra(usuario, nombreEspectaculo, fechaHora, comprados);

        notificaciones.avisar(new EventoFuncion(
                "Compra",
                "Compra confirmada para " + usuario.getNombre() + " (" + comprados.size() + " asientos)",
                nombreEspectaculo,
                fechaHora
        ));

        return compra;
    }

    private Asiento buscarAsiento(String codigo) {
        for (Asiento a : asientos) {
            if (a.getCodigo().equalsIgnoreCase(codigo)) {
                return a;
            }
        }
        throw new IllegalArgumentException("No existe el asiento: " + codigo);
    }
}
