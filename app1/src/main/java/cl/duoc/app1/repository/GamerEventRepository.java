package cl.duoc.app1.repository;

import cl.duoc.app1.model.GamerEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface GamerEventRepository extends JpaRepository<GamerEvent, Long> {

    // Método para buscar eventos destacados desde una fecha en adelante
    List<GamerEvent> findByDestacadoTrueAndFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);

    // Método para buscar próximos eventos generales desde una fecha en adelante
    List<GamerEvent> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);

    // Método para buscar por categoría y fecha
    List<GamerEvent> findByCategoriaIgnoreCaseAndFechaGreaterThanEqualOrderByFechaAsc(
            String categoria,
            LocalDate fecha
    );

    // Método para buscar por ubicación y fecha
    List<GamerEvent> findByUbicacionContainingIgnoreCaseAndFechaGreaterThanEqualOrderByFechaAsc(
            String ubicacion,
            LocalDate fecha
    );

    // Método para a buscar por nombre/título
    List<GamerEvent> findByTituloContainingIgnoreCaseAndFechaGreaterThanEqualOrderByFechaAsc(
            String titulo, 
            LocalDate fecha
    );
}