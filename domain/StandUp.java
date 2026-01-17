package domain;

public class StandUp extends Espectaculo {
    public StandUp(String nombre) {
        super(nombre);
    }

    @Override
    public String getTipo() {
        return "StandUp";
    }
}
