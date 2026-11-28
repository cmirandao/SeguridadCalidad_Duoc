
package cl.duoc.app1.controller;

import cl.duoc.app1.model.GamerEvent;
import cl.duoc.app1.repository.AppUserRepository;
import cl.duoc.app1.repository.GamerEventRepository;
import cl.duoc.app1.security.TokenStore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebControllerTest {

    @Mock
    private GamerEventRepository gamerEventRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Test
    void testCargarPaginaInicio() {
        WebController controller = new WebController(
                gamerEventRepository,
                appUserRepository,
                new TokenStore()
        );

        GamerEvent eventoDestacado = new GamerEvent();
        eventoDestacado.setTitulo("Santiago Gaming Fest");

        GamerEvent eventoProximo = new GamerEvent();
        eventoProximo.setTitulo("Torneo de Esports");

        when(gamerEventRepository
                .findByDestacadoTrueAndFechaGreaterThanEqualOrderByFechaAsc(any()))
                .thenReturn(List.of(eventoDestacado));

        when(gamerEventRepository
                .findByFechaGreaterThanEqualOrderByFechaAsc(any()))
                .thenReturn(List.of(eventoDestacado, eventoProximo));

        ExtendedModelMap model = new ExtendedModelMap();

        String vista = controller.inicio(model);

        assertEquals("index", vista);
        assertEquals(1, ((List<?>) model.get("destacados")).size());
        assertEquals(2, ((List<?>) model.get("proximos")).size());
    }
}
