package cl.duoc.app1.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomAuthenticationProviderTest {

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private CustomAuthenticationProvider provider;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        provider = new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Test
    void authenticatesSeededUserWithoutCallingTheApplicationOverHttp() {
        var user = User.withUsername("demo@gamer.cl")
                .password("encoded-password")
                .authorities("ROLE_USER")
                .build();
        when(userDetailsService.loadUserByUsername("demo@gamer.cl")).thenReturn(user);
        when(passwordEncoder.matches("Gamer123!", "encoded-password")).thenReturn(true);

        var result = provider.authenticate(
                new UsernamePasswordAuthenticationToken("demo@gamer.cl", "Gamer123!"));

        assertEquals(user, result.getPrincipal());
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
        verify(userDetailsService).loadUserByUsername("demo@gamer.cl");
        verify(passwordEncoder).matches("Gamer123!", "encoded-password");
        verifyNoMoreInteractions(userDetailsService, passwordEncoder);
    }

    @Test
    void rejectsIncorrectPassword() {
        var user = User.withUsername("demo@gamer.cl")
                .password("encoded-password")
                .authorities("ROLE_USER")
                .build();
        when(userDetailsService.loadUserByUsername("demo@gamer.cl")).thenReturn(user);
        when(passwordEncoder.matches("incorrect", "encoded-password")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> provider.authenticate(
                new UsernamePasswordAuthenticationToken("demo@gamer.cl", "incorrect")));
    }
}
