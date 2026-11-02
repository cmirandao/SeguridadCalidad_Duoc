package cl.duoc.app1.repository;

import cl.duoc.app1.model.GamerEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GamerEventRepositoryTest {

    @Autowired
    private GamerEventRepository repository;

    @Test
    public void testGuardarYBuscarEvento() {
        GamerEvent evento = new GamerEvent();
        evento.setTitulo("Torneo Valorant");
        evento.setCategoria("Esports");
        evento.setFecha(LocalDate.now().plusDays(2));
        evento.setUbicacion("Santiago");
        repository.save(evento);

        List<GamerEvent> encontrados = repository
                .findByTituloContainingIgnoreCaseAndFechaGreaterThanEqualOrderByFechaAsc("valorant", LocalDate.now());

        assertThat(encontrados).isNotEmpty();
        assertThat(encontrados.get(0).getTitulo()).isEqualTo("Torneo Valorant");
        assertThat(encontrados.get(0).getCategoria()).isEqualTo("Esports");
    }
}