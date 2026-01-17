package domain;

public class StandUp extends Espectaculo {
    public StandUp(String nombre) {
        super(nombre);
    }

    @Override
    public TipoEspectaculo getTipo() {
        return TipoEspectaculo.STAND_UP;
    }
}
