import common.Rol;
import common.Seccion;
import domain.Asiento;
import domain.Compra;
import domain.Espectaculo;
import domain.Funcion;
import domain.Usuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import servicios.CentroNotificacionesFuncion;
import servicios.CreadorTeatro;
import servicios.EnVivoTicketsFacade;
import servicios.NotificadorAdministradorEvento;
import servicios.NotificadorUsuario;

public class DemoMain {
    public static void main(String[] args) {
        CentroNotificacionesFuncion notificaciones = new CentroNotificacionesFuncion();
        EnVivoTicketsFacade sistema = new EnVivoTicketsFacade(notificaciones);

        Usuario ana = new Usuario("Ana", "ana@correo.com", Rol.CLIENTE);
        Usuario admin = new Usuario("Marco", "admin@envivo.com", Rol.ADMINISTRADOR);

        sistema.suscribirNotificaciones(new NotificadorUsuario(ana));
        sistema.suscribirNotificaciones(new NotificadorAdministradorEvento(admin));

        Espectaculo teatro = sistema.registrarEspectaculo(new CreadorTeatro(), "La Casa de la Niebla");

        List<Asiento> mapa = new ArrayList<>();
        mapa.add(new Asiento(Seccion.PLATEA, 1, 1, 18));
        mapa.add(new Asiento(Seccion.PLATEA, 1, 2, 18));
        mapa.add(new Asiento(Seccion.BALCON, 2, 10, 12));
        mapa.add(new Asiento(Seccion.VIP, 1, 1, 35));

        Funcion f1 = sistema.crearFuncion(teatro.getNombre(), LocalDateTime.now().plusDays(3).withHour(20).withMinute(0), mapa);

        System.out.println("\nDisponibles al inicio:");
        for (Asiento a : sistema.verDisponibilidad(f1)) {
            System.out.println("- " + a);
        }

        System.out.println("\nReserva:");
        sistema.reservarAsientos(f1, Arrays.asList("PLATEA-F1-1", "VIP-F1-1"), 10);

        System.out.println("\nDisponibles luego de reservar:");
        for (Asiento a : sistema.verDisponibilidad(f1)) {
            System.out.println("- " + a);
        }

        System.out.println("\nCompra:");
        Compra compra = sistema.comprar(ana, f1, List.of("PLATEA-F1-1", "VIP-F1-1"));
        System.out.println(compra);

        System.out.println("\nReprogramacion:");
        f1.cambiarFecha(f1.getFechaHora().plusDays(1));
    }
}
