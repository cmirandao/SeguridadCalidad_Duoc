
package cl.duoc.app1.repository;

import cl.duoc.app1.model.AppUser;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Test
    void testBuscarUsuarioPorCorreoIgnorandoMayusculas() {
        String correo = "prueba-" + UUID.randomUUID() + "@gamer.cl";

        AppUser usuario = new AppUser();
        usuario.setNombre("Usuario de Prueba");
        usuario.setEmail(correo);
        usuario.setUsername("gamer-" + UUID.randomUUID());
        usuario.setPassword("password-encriptada");
        usuario.setRoles(java.util.List.of("ROLE_USER"));
        usuario.setNotificaciones(true);

        userRepository.saveAndFlush(usuario);

        Optional<AppUser> resultado =
                userRepository.findByEmailIgnoreCase(correo.toUpperCase());

        assertTrue(resultado.isPresent());
        assertEquals(correo, resultado.get().getEmail());
        assertEquals("Usuario de Prueba", resultado.get().getNombre());
    }
}
