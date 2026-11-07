package cl.duoc.app1.controller;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.model.GamerEvent;
import cl.duoc.app1.repository.AppUserRepository;
import cl.duoc.app1.repository.GamerEventRepository;

import org.springframework.format.annotation.DateTimeFormat;
import cl.duoc.app1.security.TokenStore;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    private final GamerEventRepository gamerEventRepository;
    private final AppUserRepository appUserRepository;
    private final TokenStore tokenStore;

    public WebController(GamerEventRepository gamerEventRepository, AppUserRepository appUserRepository, TokenStore tokenStore) {
        this.gamerEventRepository = gamerEventRepository;
        this.appUserRepository = appUserRepository;
        this.tokenStore = tokenStore;
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
     */
    @GetMapping({"/", "/inicio", "/home"})
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

    /** * [PÚBLICA] Búsqueda y Filtrado de Eventos */
    @GetMapping("/eventos")
    public String buscarEventos(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "ubicacion", required = false) String ubicacion,
            Model model) {

        LocalDate hoy = LocalDate.now();
        List<GamerEvent> resultados;

        // Lógica de filtrado combinada o secuencial según lo que necesite tu
        // repositorio
        if (titulo != null && !titulo.isEmpty()) {
            resultados = gamerEventRepository
                    .findByTituloContainingIgnoreCaseAndFechaGreaterThanEqualOrderByFechaAsc(titulo, hoy);
        } else if (categoria != null && !categoria.isEmpty()) {
            resultados = gamerEventRepository
                    .findByCategoriaIgnoreCaseAndFechaGreaterThanEqualOrderByFechaAsc(categoria, hoy);
        } else if (fecha != null) {
            // Ejemplo de filtro por fecha exacta
            resultados = gamerEventRepository.findByFechaOrderByFechaAsc(fecha);
        } else if (ubicacion != null && !ubicacion.isEmpty()) {
            // Ejemplo de filtro por ubicación aproximada (ignoring case)
            resultados = gamerEventRepository.findByUbicacionContainingIgnoreCaseOrderByFechaAsc(ubicacion);
        } else {
            resultados = gamerEventRepository.findByFechaGreaterThanEqualOrderByFechaAsc(hoy);
        }

        // Enviamos los resultados y mantenemos los valores seleccionados en la vista
        model.addAttribute("eventos", resultados);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("fechaSeleccionada", fecha);
        model.addAttribute("ubicacionSeleccionada", ubicacion);

        return "eventos";
    }

    /**
     * [PRIVADA] Detalle de Eventos
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

    /**
     * [API REST] Endpoint interno consumido por RestTemplate para la integración JWT
     */
    @org.springframework.web.bind.annotation.ResponseBody
    @org.springframework.web.bind.annotation.GetMapping("/api/greetings")
    public ResponseEntity<String> apiGreeting(@RequestParam(name="name", required=false, defaultValue="Usuario") String name) {
        return ResponseEntity.ok("¡Hola, " + name + "! Acceso autorizado con éxito mediante token JWT.");
    }

    /**
     * [PRIVADA] Endpoint de integración JWT
     */
    @GetMapping("/greetings")
    public String greeting(Authentication authentication, Model model) {  
        String displayName = "Usuario";

        if (authentication != null && authentication.isAuthenticated()) {
            String principalName = authentication.getName();
            
            AppUser usuario = appUserRepository.findByUsernameIgnoreCase(principalName)
                .or(() -> appUserRepository.findByEmailIgnoreCase(principalName))
                .orElse(null);

            if (usuario != null && usuario.getNombre() != null) {
                displayName = usuario.getNombre();
            }
        }

        String url = "http://localhost:8081/api/greetings?name={name}"; 
        final var restTemplate = new RestTemplate();
        String token = tokenStore.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            entity, 
            String.class, 
            displayName
        );
            
        model.addAttribute("name", response.getBody());  
        return "greetings";  
    }
}