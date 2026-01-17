package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Espectaculo {
    private final String nombre;
    private final List<Funcion> funciones;

    protected Espectaculo(String nombre) {
        this.nombre = Objects.requireNonNull(nombre);
        this.funciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Funcion> getFunciones() {
        return Collections.unmodifiableList(funciones);
    }

    public void agregarFuncion(Funcion funcion) {
        funciones.add(Objects.requireNonNull(funcion));
    }

    public abstract TipoEspectaculo getTipo();
}

