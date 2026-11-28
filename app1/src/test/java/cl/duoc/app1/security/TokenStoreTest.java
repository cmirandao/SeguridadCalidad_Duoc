
package cl.duoc.app1.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenStoreTest {

    @Test
    void testGuardarYObtenerToken() {
        TokenStore tokenStore = new TokenStore();

        String tokenEsperado = "token-jwt-prueba";

        tokenStore.setToken(tokenEsperado);

        assertEquals(tokenEsperado, tokenStore.getToken());
    }
}
