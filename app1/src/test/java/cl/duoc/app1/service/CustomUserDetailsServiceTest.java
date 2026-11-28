
package cl.duoc.app1.service;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.repository.AppUserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void testCargarUsuarioPorCorreo() {
        AppUser usuario = new AppUser();

        usuario.setNombre("Usuario Prueba");
        usuario.setEmail("prueba@gamer.cl");
        usuario.setUsername("gamer123");
        usuario.setPassword("Password123");
        usuario.setRoles(List.of("ROLE_USER"));

        when(userRepository.findByEmailIgnoreCase("prueba@gamer.cl"))
                .thenReturn(Optional.of(usuario));

        UserDetails resultado =
                userDetailsService.loadUserByUsername(" prueba@gamer.cl ");

        assertEquals("prueba@gamer.cl", resultado.getUsername());
        assertEquals("Password123", resultado.getPassword());
        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_USER")));
    }
}
