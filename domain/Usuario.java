package domain;

import common.Rol;
import java.util.Objects;

public class Usuario {
    private final String nombre;
    private final String correo;
    private final Rol rol;

    public Usuario(String nombre, String correo, Rol rol) {
        this.nombre = Objects.requireNonNull(nombre);
        this.correo = Objects.requireNonNull(correo);
        this.rol = Objects.requireNonNull(rol);
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean esAdministrador() {
        return this.rol.equals(Rol.ADMINISTRADOR);
    }

    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }
}
