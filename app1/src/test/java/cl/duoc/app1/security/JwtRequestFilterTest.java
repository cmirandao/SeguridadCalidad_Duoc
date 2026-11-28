
package cl.duoc.app1.security;

import cl.duoc.app1.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private FilterChain filterChain;

    @Test
    void testAutenticarSolicitudConTokenValido() throws Exception {
        JwtRequestFilter filter = new JwtRequestFilter(
                userDetailsService,
                jwtTokenUtil
        );

        String token = "jwt-de-prueba";

        UserDetails usuario = User.builder()
                .username("usuario@gamer.cl")
                .password("password-encriptada")
                .authorities(List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                ))
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtTokenUtil.extractUsername(token))
                .thenReturn("usuario@gamer.cl");

        when(userDetailsService.loadUserByUsername("usuario@gamer.cl"))
                .thenReturn(usuario);

        when(jwtTokenUtil.validateToken(token, usuario))
                .thenReturn(true);

        SecurityContextHolder.clearContext();

        try {
            filter.doFilterInternal(request, response, filterChain);

            assertEquals(
                    "usuario@gamer.cl",
                    SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getName()
            );

            verify(filterChain).doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
