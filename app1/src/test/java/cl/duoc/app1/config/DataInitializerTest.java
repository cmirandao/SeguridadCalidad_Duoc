
package cl.duoc.app1.config;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.repository.AppUserRepository;
import cl.duoc.app1.repository.GamerEventRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private GamerEventRepository eventRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testInicializarUsuariosDeDemostracion() throws Exception {
        DataInitializer initializer = new DataInitializer();

        when(userRepository.findByEmailIgnoreCase(any(String.class)))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("Gamer123!"))
                .thenReturn("password-encriptada");

        // Evita crear eventos en esta prueba.
        when(eventRepository.count()).thenReturn(1L);

        CommandLineRunner runner = initializer.inicializarDatos(
                userRepository,
                eventRepository,
                passwordEncoder
        );

        runner.run();

        ArgumentCaptor<AppUser> captor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(userRepository, org.mockito.Mockito.times(3))
                .save(captor.capture());

        assertEquals(3, captor.getAllValues().size());

        assertEquals(
                "demo@gamer.cl",
                captor.getAllValues().get(0).getEmail()
        );

        assertEquals(
                "ana@gamer.cl",
                captor.getAllValues().get(1).getEmail()
        );

        assertEquals(
                "marco@gamer.cl",
                captor.getAllValues().get(2).getEmail()
        );

        assertTrue(captor.getAllValues().get(0).isNotificaciones());
    }
}
