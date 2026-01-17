package servicios;

import common.EventoFuncion;
import domain.Usuario;
import ports.ObservadorFuncion;

public class NotificadorAdministradorEvento implements ObservadorFuncion {
    private final Usuario administrador;

    public NotificadorAdministradorEvento(Usuario administrador) {
        this.administrador = administrador;
    }

    @Override
    public void notificar(EventoFuncion evento) {
        System.out.println("[Admin " + administrador.getNombre() + "] " +
                evento.getTipo() + ": " + evento.getMensaje() +
                " | " + evento.getNombreEspectaculo() + " @ " + evento.getFechaHora());
    }
}
