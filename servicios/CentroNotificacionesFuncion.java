package servicios;

import common.EventoFuncion;
import domain.ServicioNotificacion;
import ports.ObservadorFuncion;
import ports.SujetoFuncion;
import java.util.ArrayList;
import java.util.List;

public class CentroNotificacionesFuncion implements SujetoFuncion, ServicioNotificacion {
    private final List<ObservadorFuncion> observadores;

    public CentroNotificacionesFuncion() {
        observadores = new ArrayList<>();
    }

    @Override
    public void suscribir(ObservadorFuncion observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void desuscribir(ObservadorFuncion observador) {
        observadores.remove(observador);
    }

    @Override
    public void avisar(EventoFuncion evento) {
        List<ObservadorFuncion> copia = new ArrayList<>(observadores);
        for (ObservadorFuncion o : copia) {
            o.notificar(evento);
        }
    }
}
