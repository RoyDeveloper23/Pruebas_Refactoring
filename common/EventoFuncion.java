package common;

import java.time.LocalDateTime;

public class EventoFuncion {
    private final String tipo;
    private final String mensaje;
    private final String nombreEspectaculo;
    private final LocalDateTime fechaHora;

    public EventoFuncion(String tipo, String mensaje, String nombreEspectaculo, LocalDateTime fechaHora) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.nombreEspectaculo = nombreEspectaculo;
        this.fechaHora = fechaHora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getNombreEspectaculo() {
        return nombreEspectaculo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String obtenerResumenLog() {
        return String.format("[%s] %s - %s (%s)", 
            fechaHora, tipo.toUpperCase(), mensaje, nombreEspectaculo);
    }
}
