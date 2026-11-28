package cl.duoc.app1.service;

public interface RegistrationService {
    void registrar(
            String nombre,
            String email,
            String username,
            String password,
            String confirmPassword
    );
}