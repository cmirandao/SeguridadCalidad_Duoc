package cl.duoc.app1.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void testGenerarYValidarToken() {
        UserDetails userDetails = new User("usuario@gamer.cl", "password123", new ArrayList<>());

        String token = jwtTokenUtil.generateToken(userDetails);
        assertNotNull(token, "El token no debería ser nulo");

        String username = jwtTokenUtil.extractUsername(token);
        assertEquals("usuario@gamer.cl", username, "El usuario extraído debe coincidir");

        boolean isValido = jwtTokenUtil.validateToken(token, userDetails);
        assertTrue(isValido, "El token debería ser válido para este usuario");
    }
}