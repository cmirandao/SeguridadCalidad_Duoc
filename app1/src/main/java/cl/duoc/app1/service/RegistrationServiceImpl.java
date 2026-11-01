package cl.duoc.app1.service;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void registrar(
            String nombre,
            String email,
            String username,
            String password,
            String confirmPassword) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                "Debes ingresar tu nombre."
            );
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                "Debes ingresar tu correo electrónico."
            );
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                "Debes ingresar un nombre de usuario."
            );
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException(
                "El correo electrónico no tiene un formato válido."
            );
        }

        if (password == null || password.length() < 8
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException(
                "La contraseña debe tener al menos 8 caracteres, "
                + "una mayúscula, una minúscula y un número."
            );
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                "Las contraseñas no coinciden."
            );
        }

        if (userRepository.existsByEmailIgnoreCase(email.trim())) {
            throw new IllegalArgumentException(
                "Ese correo electrónico ya está registrado."
            );
        }

        if (userRepository.existsByUsernameIgnoreCase(username.trim())) {
            throw new IllegalArgumentException(
                "Ese nombre de usuario ya está ocupado."
            );
        }

        AppUser usuario = new AppUser();
        usuario.setNombre(nombre.trim());
        usuario.setEmail(email.trim().toLowerCase());
        usuario.setUsername(username.trim());
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRoles(new java.util.ArrayList<>(java.util.List.of("ROLE_USER")));
        usuario.setNotificaciones(true);

        userRepository.save(usuario);
    }
}