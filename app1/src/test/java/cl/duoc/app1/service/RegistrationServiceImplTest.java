
package cl.duoc.app1.service;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.repository.AppUserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void testRegistrarUsuarioCorrectamente() {
        when(userRepository.existsByEmailIgnoreCase("prueba@gamer.cl"))
                .thenReturn(false);

        when(userRepository.existsByUsernameIgnoreCase("gamer123"))
                .thenReturn(false);

        when(passwordEncoder.encode("Password123"))
                .thenReturn("password-encriptada");

        registrationService.registrar(
                "Usuario Prueba",
                "prueba@gamer.cl",
                "gamer123",
                "Password123",
                "Password123"
        );

        ArgumentCaptor<AppUser> captor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(userRepository).save(captor.capture());

        AppUser usuarioGuardado = captor.getValue();

        assertEquals("Usuario Prueba", usuarioGuardado.getNombre());
        assertEquals("prueba@gamer.cl", usuarioGuardado.getEmail());
        assertEquals("gamer123", usuarioGuardado.getUsername());
        assertEquals("password-encriptada", usuarioGuardado.getPassword());
        assertEquals("ROLE_USER", usuarioGuardado.getRoles().get(0));
        assertTrue(usuarioGuardado.isNotificaciones());
    }
}
