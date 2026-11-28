
package cl.duoc.app1.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    @Test
    void testSetNombreYGetNombre() {

        // Arrange
        AppUser usuario = new AppUser();

        // Act
        usuario.setNombre("Usuario Demo");

        // Assert
        assertEquals("Usuario Demo", usuario.getNombre());
    }
}
