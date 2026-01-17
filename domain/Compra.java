package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Compra {
    private final Usuario usuario;
    private final String nombreEspectaculo;
    private final LocalDateTime fechaHora;
    private final List<Asiento> asientos;

    public Compra(Usuario usuario, DatosFuncion datosFuncion, List<Asiento> asientos) {
        this.usuario = Objects.requireNonNull(usuario);
        this.nombreEspectaculo = Objects.requireNonNull(datosFuncion.nombre());
        this.fechaHora = Objects.requireNonNull(datosFuncion.fecha());
        this.asientos = new ArrayList<>(Objects.requireNonNull(asientos));
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    public double total() {
        double suma = 0;
        for (Asiento a : asientos) {
            suma += a.getPrecio();
        }
        return suma;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "usuario=" + usuario.getNombre() +
                ", espectaculo='" + nombreEspectaculo + '\'' +
                ", fechaHora=" + fechaHora +
                ", asientos=" + asientos.size() +
                ", total=$" + total() +
                '}';
    }
}
