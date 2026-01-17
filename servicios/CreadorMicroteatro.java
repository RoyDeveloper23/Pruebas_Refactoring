package servicios;

import domain.Espectaculo;
import domain.Microteatro;

public class CreadorMicroteatro extends CreadorEspectaculo {
    @Override
    protected Espectaculo crearEspectaculo(String nombre) {
        return new Microteatro(nombre);
    }
}
