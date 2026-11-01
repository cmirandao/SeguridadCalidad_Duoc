package cl.duoc.app1.config;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.model.GamerEvent;
import cl.duoc.app1.repository.AppUserRepository;
import cl.duoc.app1.repository.GamerEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner inicializarDatos(
            AppUserRepository userRepository,
            GamerEventRepository eventRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            crearOActualizarUsuario(
                userRepository,
                passwordEncoder,
                "Usuario Demo",
                "demo@gamer.cl",
                "demogamer",
                "Gamer123!"
            );

            crearOActualizarUsuario(
                userRepository,
                passwordEncoder,
                "Ana Gamer",
                "ana@gamer.cl",
                "anagamer",
                "Gamer123!"
            );

            crearOActualizarUsuario(
                userRepository,
                passwordEncoder,
                "Marco Gamer",
                "marco@gamer.cl",
                "marcogamer",
                "Gamer123!"
            );

            if (eventRepository.count() == 0) {

                GamerEvent evento1 = new GamerEvent();
                evento1.setTitulo("Santiago Gaming Fest");
                evento1.setDescripcion(
                    "Encuentro gamer con torneos, concursos, "
                    + "juegos competitivos y actividades para la comunidad."
                );
                evento1.setCategoria("Gaming");
                evento1.setFecha(LocalDate.now().plusDays(15));
                evento1.setHora(LocalTime.of(10, 0));
                evento1.setUbicacion("Santiago");
                evento1.setDireccion(
                    "Centro de eventos, Santiago, Chile"
                );
                evento1.setOrganizadores("Comunidad Gamer Chile");
                evento1.setEstacionamiento(true);
                evento1.setLocomocion(true);
                evento1.setComidas(true);
                evento1.setAlojamiento(false);
                evento1.setExpositores(
                    "Creadores de contenido, equipos de esports "
                    + "y tiendas especializadas."
                );
                evento1.setDestacado(true);

                GamerEvent evento2 = new GamerEvent();
                evento2.setTitulo("Torneo de Esports");
                evento2.setDescripcion(
                    "Competencia de videojuegos con inscripción "
                    + "para equipos y jugadores individuales."
                );
                evento2.setCategoria("Esports");
                evento2.setFecha(LocalDate.now().plusDays(25));
                evento2.setHora(LocalTime.of(12, 0));
                evento2.setUbicacion("Valparaíso");
                evento2.setDireccion(
                    "Recinto de competencias, Valparaíso, Chile"
                );
                evento2.setOrganizadores("Esports Chile");
                evento2.setEstacionamiento(true);
                evento2.setLocomocion(true);
                evento2.setComidas(true);
                evento2.setAlojamiento(true);
                evento2.setExpositores(
                    "Jugadores profesionales y comentaristas."
                );
                evento2.setDestacado(true);

                GamerEvent evento3 = new GamerEvent();
                evento3.setTitulo("Feria de Juegos Indie");
                evento3.setDescripcion(
                    "Exhibición de videojuegos independientes, "
                    + "demostraciones y charlas con desarrolladores."
                );
                evento3.setCategoria("Indie");
                evento3.setFecha(LocalDate.now().plusDays(40));
                evento3.setHora(LocalTime.of(11, 0));
                evento3.setUbicacion("Concepción");
                evento3.setDireccion(
                    "Centro cultural, Concepción, Chile"
                );
                evento3.setOrganizadores("Desarrollo Indie Chile");
                evento3.setEstacionamiento(false);
                evento3.setLocomocion(true);
                evento3.setComidas(true);
                evento3.setAlojamiento(false);
                evento3.setExpositores(
                    "Estudios independientes y desarrolladores locales."
                );
                evento3.setDestacado(false);

                eventRepository.saveAll(
                    new java.util.ArrayList<>(
                        java.util.List.of(evento1, evento2, evento3)
                    )
                );
            }
        };
    }

    private void crearOActualizarUsuario(
            AppUserRepository repository,
            PasswordEncoder encoder,
            String nombre,
            String email,
            String username,
            String password) {

        AppUser usuario = repository.findByEmailIgnoreCase(email)
            .orElseGet(AppUser::new);

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setUsername(username);
        usuario.setPassword(encoder.encode(password));
        usuario.setRoles(List.of("ROLE_USER"));
        usuario.setNotificaciones(true);

        repository.save(usuario);

        System.out.println("Usuario de prueba configurado: " + email);
        System.out.println("Contraseña válida: "
                + encoder.matches(password, usuario.getPassword()));
    }
}
