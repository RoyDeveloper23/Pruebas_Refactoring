package domain;

public class Microteatro extends Espectaculo {
    public Microteatro(String nombre) {
        super(nombre);
    }

    @Override
    public String getTipo() {
        return "Microteatro";
    }
}
