package servicios;

import domain.Espectaculo;
import domain.StandUp;

public class CreadorStandUp extends CreadorEspectaculo {
    @Override
    protected Espectaculo crearEspectaculo(String nombre) {
        return new StandUp(nombre);
    }
}
