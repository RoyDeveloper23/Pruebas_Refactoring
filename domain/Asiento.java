package domain;

import common.EstadoAsiento;
import common.MensajesError;
import common.Seccion;
import java.time.Instant;

public class Asiento {
    private final Seccion seccion;
    private final int fila;
    private final int numero;
    private final double precio;

    private EstadoAsiento estado;
    private Instant reservadoHasta;

    public Asiento(Seccion seccion, int fila, int numero, double precio) {
        this.seccion = seccion;
        this.fila = fila;
        this.numero = numero;
        this.precio = precio;
        this.estado = EstadoAsiento.DISPONIBLE;
        this.reservadoHasta = null;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public int getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public double getPrecio() {
        return precio;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public String getCodigo() {
        return seccion + "-F" + fila + "-" + numero;
    }

    public boolean estaDisponible(Instant ahora) {
        if (estado == EstadoAsiento.DISPONIBLE) {
            return true;
        }
        if (estado == EstadoAsiento.RESERVADO && reservadoHasta != null && ahora.isAfter(reservadoHasta)) {
            estado = EstadoAsiento.DISPONIBLE;
            reservadoHasta = null;
            return true;
        }
        return false;
    }

    public void reservarHasta(Instant hasta) {
        if (estado == EstadoAsiento.AGOTADO) {
            throw new IllegalStateException(MensajesError.ASIENTO_AGOTADO);
        }
        estado = EstadoAsiento.RESERVADO;
        reservadoHasta = hasta;
    }

    public void liberarReserva() {
        if (estado == EstadoAsiento.RESERVADO) {
            estado = EstadoAsiento.DISPONIBLE;
            reservadoHasta = null;
        }
    }

    public void confirmarCompra() {
        if (estado == EstadoAsiento.AGOTADO) {
            throw new IllegalStateException(MensajesError.ASIENTO_AGOTADO);
        }
        estado = EstadoAsiento.AGOTADO;
        reservadoHasta = null;
    }

    @Override
    public String toString() {
        return getCodigo() + " $" + precio + " [" + estado + "]";
    }
}
