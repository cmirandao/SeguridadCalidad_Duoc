package cl.duoc.app1.service;

import cl.duoc.app1.model.AppUser;
import cl.duoc.app1.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public CustomUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        String correo = email.trim();

        System.out.println(
            "[LOGIN] Buscando usuario por correo: " + correo
        );

        AppUser usuario = userRepository.findByEmailIgnoreCase(correo)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "Usuario no encontrado: " + correo
                )
            );

        System.out.println(
            "[LOGIN] Usuario encontrado: " + usuario.getEmail()
        );

        List<SimpleGrantedAuthority> authorities =
            usuario.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        System.out.println(
            "[LOGIN] Roles cargados: " + authorities.size()
        );

        return User.builder()
            .username(usuario.getEmail())
            .password(usuario.getPassword())
            .authorities(authorities)
            .build();
    }
}