
package cl.duoc.app1.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamerEventTest {

    @Test
    void testSetTituloYGetTitulo() {

        // Arrange
        GamerEvent evento = new GamerEvent();

        // Act
        evento.setTitulo("Santiago Gaming Fest");

        // Assert
        assertEquals("Santiago Gaming Fest", evento.getTitulo());
    }
}
