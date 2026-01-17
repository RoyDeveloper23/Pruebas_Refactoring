package servicios;

import domain.Espectaculo;

public abstract class CreadorEspectaculo {
    public Espectaculo crear(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        return crearEspectaculo(nombre.trim());
    }

    protected abstract Espectaculo crearEspectaculo(String nombre);
}
