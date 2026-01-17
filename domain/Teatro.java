package domain;

public class Teatro extends Espectaculo {
    public Teatro(String nombre) {
        super(nombre);
    }

    @Override
    public TipoEspectaculo getTipo() {
        return TipoEspectaculo.TEATRO;
    }
}
