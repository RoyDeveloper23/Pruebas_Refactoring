package servicios;

import domain.Espectaculo;
import domain.Teatro;

public class CreadorTeatro extends CreadorEspectaculo {
    @Override
    protected Espectaculo crearEspectaculo(String nombre) {
        return new Teatro(nombre);
    }
}
