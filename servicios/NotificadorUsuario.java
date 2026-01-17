package servicios;

import common.EventoFuncion;
import domain.Usuario;
import ports.ObservadorFuncion;

public class NotificadorUsuario implements ObservadorFuncion {
    private final Usuario usuario;

    public NotificadorUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void notificar(EventoFuncion evento) {
        System.out.println("[Aviso a " + usuario.getNombre() + "] " +
                evento.getTipo() + ": " + evento.getMensaje() +
                " | " + evento.getNombreEspectaculo() + " @ " + evento.getFechaHora());
    }
}
