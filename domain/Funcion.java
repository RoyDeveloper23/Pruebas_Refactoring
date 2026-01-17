package domain;

import common.EstadoAsiento;
import common.EventoFuncion;
import common.MensajesError;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Funcion {
    private final String nombreEspectaculo;
    private LocalDateTime fechaHora;
    private final List<Asiento> asientos;
    private final ServicioNotificacion notificaciones;

    public Funcion(String nombreEspectaculo, LocalDateTime fechaHora, List<Asiento> asientos, ServicioNotificacion notificaciones) {
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
        notificarEvento("Reprogramacion", "La funcion cambio de fecha/hora");
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
        validarDuracion(duracion);
        liberarReservasVencidas();

        List<Asiento> seleccion = obtenerAsientosParaReserva(codigosAsiento);

        Instant hasta = Instant.now().plus(duracion);
        for (Asiento a : seleccion) {
            a.reservarHasta(hasta);
        }

        notificarEvento("Reserva", "Se reservaron " + seleccion.size() + " asientos por " + duracion.toMinutes() + " minutos");

        return seleccion;
    }

    public Compra confirmarCompra(Usuario usuario, List<String> codigosAsiento) {
        Objects.requireNonNull(usuario);
        Objects.requireNonNull(codigosAsiento);

        liberarReservasVencidas();

        List<Asiento> comprados = obtenerAsientosParaCompra(codigosAsiento);

        for (Asiento a : comprados) {
            a.confirmarCompra();
        }

        Compra compra = new Compra(usuario, new DatosFuncion(nombreEspectaculo, fechaHora), comprados);

        notificarEvento("Compra", "Compra confirmada para " + usuario.getNombre() + " (" + comprados.size() + " asientos)");

        return compra;
    }

    private void notificarEvento(String tipo, String mensaje) {
        notificaciones.avisar(new EventoFuncion(tipo, mensaje, nombreEspectaculo, fechaHora));
    }

    private void validarDuracion(Duration duracion) {
        if (duracion == null || duracion.isZero() || duracion.isNegative()) {
            throw new IllegalArgumentException("La duracion debe ser positiva");
        }
    }

    private List<Asiento> obtenerAsientosParaReserva(List<String> codigos) {
        List<Asiento> seleccion = new ArrayList<>();
        for (String codigo : codigos) {
            Asiento a = buscarAsiento(codigo);
            if (a.getEstado() != EstadoAsiento.DISPONIBLE) {
                throw new IllegalStateException(String.format(MensajesError.ASIENTO_NO_DISPONIBLE, codigo));
            }
            seleccion.add(a);
        }
        return seleccion;
    }

    private List<Asiento> obtenerAsientosParaCompra(List<String> codigos) {
        List<Asiento> comprados = new ArrayList<>();
        for (String codigo : codigos) {
            Asiento a = buscarAsiento(codigo);
            if (a.getEstado() == EstadoAsiento.AGOTADO) {
                throw new IllegalStateException(String.format(MensajesError.ASIENTO_YA_AGOTADO, codigo));
            }
            if (a.getEstado() == EstadoAsiento.DISPONIBLE) {
                throw new IllegalStateException(String.format(MensajesError.ASIENTO_NO_RESERVADO, codigo));
            }
            comprados.add(a);
        }
        return comprados;
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
