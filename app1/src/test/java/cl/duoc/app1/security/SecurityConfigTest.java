
package cl.duoc.app1.security;

import org.junit.jupiter.api.Test;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigTest {

    @Test
    void testCodificadorDeContrasenasBCrypt() {
        JwtRequestFilter filtro = new JwtRequestFilter(null, null);

        SecurityConfig config = new SecurityConfig(filtro);

        PasswordEncoder encoder = config.passwordEncoder();

        String password = "Gamer123!";
        String passwordEncriptada = encoder.encode(password);

        assertNotEquals(password, passwordEncriptada);
        assertTrue(encoder.matches(password, passwordEncriptada));
    }
}
