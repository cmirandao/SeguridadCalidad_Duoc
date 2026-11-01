package cl.duoc.app1.controller;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.model.GamerEvent;
import cl.duoc.app1.repository.AppUserRepository;
import cl.duoc.app1.repository.GamerEventRepository;
import cl.duoc.app1.service.RegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
public class WebController {

    private final AppUserRepository userRepository;
    private final GamerEventRepository eventRepository;
    private final RegistrationService registrationService;

    public WebController(
            AppUserRepository userRepository,
            GamerEventRepository eventRepository,
            RegistrationService registrationService) {

        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.registrationService = registrationService;
    }

    @GetMapping({"/", "/recetas"})
    public String inicio(Model model) {

        LocalDate hoy = LocalDate.now();

        List<GamerEvent> destacados = eventRepository
            .findByDestacadoTrueAndFechaGreaterThanEqualOrderByFechaAsc(hoy);

        List<GamerEvent> proximos = eventRepository
            .findByFechaGreaterThanEqualOrderByFechaAsc(hoy)
            .stream()
            .limit(6)
            .toList();

        model.addAttribute("destacados", destacados);
        model.addAttribute("proximos", proximos);

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        try {
            registrationService.registrar(
                nombre,
                email,
                username,
                password,
                confirmPassword
            );

            redirectAttributes.addFlashAttribute(
                "mensaje",
                "Tu cuenta fue creada. Ya puedes iniciar sesión."
            );

            return "redirect:/login";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                "error",
                e.getMessage()
            );

            return "redirect:/registro";
        }
    }

    @GetMapping("/eventos")
    public String eventos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String fecha,
            Model model) {

        LocalDate hoy = LocalDate.now();

        List<GamerEvent> resultado = eventRepository.findAll()
            .stream()
            .filter(evento -> !evento.getFecha().isBefore(hoy))
            .filter(evento ->
                categoria == null || categoria.isBlank()
                || evento.getCategoria().equalsIgnoreCase(categoria)
            )
            .filter(evento ->
                ubicacion == null || ubicacion.isBlank()
                || evento.getUbicacion()
                    .toLowerCase(Locale.ROOT)
                    .contains(ubicacion.trim().toLowerCase(Locale.ROOT))
            )
            .filter(evento -> {
                if (fecha == null || fecha.isBlank()) {
                    return true;
                }

                try {
                    return evento.getFecha().equals(LocalDate.parse(fecha));
                } catch (Exception e) {
                    return false;
                }
            })
            .sorted(Comparator.comparing(evento -> evento.getFecha()))
            .toList();

        model.addAttribute("eventos", resultado);
        model.addAttribute("categoriaSeleccionada",
            categoria == null ? "" : categoria);
        model.addAttribute("ubicacionSeleccionada",
            ubicacion == null ? "" : ubicacion);
        model.addAttribute("fechaSeleccionada",
            fecha == null ? "" : fecha);

        return "eventos";
    }

    @GetMapping("/eventos/{id}")
    public String detalleEvento(
            @PathVariable long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        GamerEvent evento = eventRepository.findById(id).orElse(null);

        if (evento == null) {
            redirectAttributes.addFlashAttribute(
                "error",
                "El evento solicitado no existe."
            );

            return "redirect:/eventos";
        }

        model.addAttribute("evento", evento);

        return "detalle-evento";
    }

    @GetMapping("/perfil")
    public String perfil(Authentication authentication, Model model) {

        AppUser usuario = obtenerUsuarioActual(authentication);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "perfil";
    }

    @PostMapping("/perfil")
    public String guardarPerfil(
            Authentication authentication,
            @RequestParam String username,
            @RequestParam(required = false) String juegosInteres,
            @RequestParam(required = false) String avatarUrl,
            @RequestParam(defaultValue = "false") boolean notificaciones,
            RedirectAttributes redirectAttributes) {

        AppUser usuario = obtenerUsuarioActual(authentication);

        if (usuario == null) {
            return "redirect:/login";
        }

        String nombreUsuario = username == null ? "" : username.trim();

        if (nombreUsuario.isBlank()) {
            redirectAttributes.addFlashAttribute(
                "error",
                "El nombre de usuario es obligatorio."
            );

            return "redirect:/perfil";
        }

        boolean usernameDuplicado =
            userRepository.findByUsernameIgnoreCase(nombreUsuario)
                .filter(otro -> !otro.getEmail().equalsIgnoreCase(
                    usuario.getEmail()))
                .isPresent();

        if (usernameDuplicado) {
            redirectAttributes.addFlashAttribute(
                "error",
                "Ese nombre de usuario ya está ocupado."
            );

            return "redirect:/perfil";
        }

        usuario.setUsername(nombreUsuario);
        usuario.setJuegosInteres(
            juegosInteres == null ? "" : juegosInteres.trim()
        );
        usuario.setAvatarUrl(
            avatarUrl == null ? "" : avatarUrl.trim()
        );
        usuario.setNotificaciones(notificaciones);

        userRepository.save(usuario);

        redirectAttributes.addFlashAttribute(
            "mensaje",
            "Tu perfil fue actualizado correctamente."
        );

        return "redirect:/perfil";
    }

    private AppUser obtenerUsuarioActual(
            Authentication authentication) {

        if (authentication == null) {
            return null;
        }

        return userRepository.findByEmailIgnoreCase(
            authentication.getName()
        ).orElse(null);
    }
}