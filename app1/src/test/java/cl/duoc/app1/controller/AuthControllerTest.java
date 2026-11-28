
package cl.duoc.app1.controller;

import cl.duoc.app1.repository.AppUserRepository;
import cl.duoc.app1.security.JwtTokenUtil;
import cl.duoc.app1.security.TokenStore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Test
    void testLoginApiExitoso() {
        TokenStore tokenStore = new TokenStore();

        AuthController controller = new AuthController(
                authenticationManager,
                userDetailsService,
                jwtTokenUtil,
                userRepository,
                passwordEncoder,
                tokenStore
        );

        UserDetails usuario = User.withUsername("usuario@gamer.cl")
                .password("password-encriptada")
                .authorities("ROLE_USER")
                .build();

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userDetailsService.loadUserByUsername("usuario"))
                .thenReturn(usuario);

        when(jwtTokenUtil.generateToken(usuario))
                .thenReturn("jwt-de-prueba");

        ResponseEntity<String> respuesta =
                controller.loginApi("usuario", "Password123");

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals("jwt-de-prueba", respuesta.getBody());
        assertEquals("jwt-de-prueba", tokenStore.getToken());
    }
}
