package cl.duoc.app1.controller;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.repository.AppUserRepository;
import cl.duoc.app1.security.JwtTokenUtil;
import cl.duoc.app1.security.TokenStore;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;

    AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, AppUserRepository userRepository, PasswordEncoder passwordEncoder, TokenStore tokenStore) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    // Endpoint tradicional por formulario web (Usa Cookie HttpOnly)
    @PostMapping("/login")
    public String loginWeb(
            @RequestParam("username") String username, 
            @RequestParam("password") String password,
            HttpServletResponse response) {
        
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            String jwt = jwtTokenUtil.generateToken(userDetails);
            tokenStore.setToken(jwt);
            
            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60);

            response.addCookie(jwtCookie);
            return "redirect:/inicio";

        } catch (Exception e) {
            return "redirect:/login?error";
        }
    }

    // Endpoint REST: Devuelve el token en texto plano JSON/String
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<String> loginApi(
            @RequestParam("user") String username, 
            @RequestParam("encryptedPass") String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String jwt = jwtTokenUtil.generateToken(userDetails);
            tokenStore.setToken(jwt);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam("nombre") String nombre,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {

        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            model.addAttribute("error", "El correo electrónico ya está registrado");
            return "registro"; 
        }

        AppUser nuevoUsuario = new AppUser();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRoles(java.util.List.of("ROLE_USER"));
        nuevoUsuario.setNotificaciones(true);

        userRepository.save(nuevoUsuario);

        return "redirect:/login?registrado=true";
    }
}