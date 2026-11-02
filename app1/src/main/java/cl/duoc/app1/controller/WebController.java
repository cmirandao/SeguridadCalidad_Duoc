package cl.duoc.app1.controller;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.model.GamerEvent;
import cl.duoc.app1.repository.AppUserRepository;
import cl.duoc.app1.repository.GamerEventRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    private final GamerEventRepository gamerEventRepository;
    private final AppUserRepository appUserRepository;

    WebController(GamerEventRepository gamerEventRepository, AppUserRepository appUserRepository) {
        this.gamerEventRepository = gamerEventRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            AppUser usuario = appUserRepository.findByUsernameIgnoreCase(username).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        return "perfil"; 
    }

    /**
     * [PÚBLICA] Página de Inicio
     * Muestra los eventos destacados y los próximos eventos generales.
     */
    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        LocalDate hoy = LocalDate.now();
        
        List<GamerEvent> destacados = gamerEventRepository
                .findByDestacadoTrueAndFechaGreaterThanEqualOrderByFechaAsc(hoy);
        
        List<GamerEvent> proximos = gamerEventRepository
                .findByFechaGreaterThanEqualOrderByFechaAsc(hoy);

        model.addAttribute("destacados", destacados);
        model.addAttribute("proximos", proximos);
        
        return "index";
    }

    /**
     * [PÚBLICA] Búsqueda y Filtrado de Eventos
     * Recibe parámetros por URL para filtrar los resultados de la base de datos.
     */
    @GetMapping("/eventos")
    public String buscarEventos(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "categoria", required = false) String categoria,
            Model model) {
        
        LocalDate hoy = LocalDate.now();
        List<GamerEvent> resultados;

        if (titulo != null && !titulo.isEmpty()) {
            resultados = gamerEventRepository
                    .findByTituloContainingIgnoreCaseAndFechaGreaterThanEqualOrderByFechaAsc(titulo, hoy);
        } else if (categoria != null && !categoria.isEmpty()) {
            resultados = gamerEventRepository
                    .findByCategoriaIgnoreCaseAndFechaGreaterThanEqualOrderByFechaAsc(categoria, hoy);
        } else {
            resultados = gamerEventRepository.findByFechaGreaterThanEqualOrderByFechaAsc(hoy);
        }

        model.addAttribute("eventos", resultados);
        return "eventos";
    }

    /**
     * [PRIVADA] Detalle de Eventos
     * Ruta protegida por SecurityConfig. Solo accesible con un JWT válido.
     */
    @GetMapping("/eventos/{id}")
    public String detalleEvento(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<GamerEvent> eventoOpt = gamerEventRepository.findById(id);
        
        if (eventoOpt.isPresent()) {
            model.addAttribute("evento", eventoOpt.get());
            return "detalle-evento";
        } else {
            redirectAttributes.addFlashAttribute("alertaPopup", "El evento seleccionado ya no está disponible.");
            return "redirect:/inicio";
        }
    }
}