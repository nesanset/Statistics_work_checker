package statisticschecker.service.auth;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statisticschecker.domain.user.AppUser;
import statisticschecker.persistence.user.*;
import statisticschecker.service.common.DomainMapper;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final DomainMapper domainMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(AppUserRepository appUserRepository, DomainMapper domainMapper) {
        this.appUserRepository = appUserRepository;
        this.domainMapper = domainMapper;
    }

    @Transactional
    public AppUser register(String username, String password) {
        username = validateUsername(username);
        validatePassword(password);
        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        AppUserEntity user = new AppUserEntity(username, passwordEncoder.encode(password));
        AppUserEntity savedUser = appUserRepository.save(user);
        return domainMapper.toAppUser(savedUser);
    }

    @Transactional
    public AppUser login(String username, String password) {
        username = validateUsername(username);
        validatePassword(password);

        Optional<AppUserEntity> user = appUserRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Неверный логин или пароль");
        }

        AppUserEntity appUser = user.get();
        if (!passwordEncoder.matches(password, appUser.getPassword())) {
            throw new IllegalArgumentException("Неверный логин или пароль");
        }
        return domainMapper.toAppUser(appUser);
    }

    private String validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Логин не должен быть пустым");
        }
        username = username.trim();
        if (username.length() < 3) {
            throw new IllegalArgumentException("Логин должен быть не короче 3 символов");
        }
        if (username.length() > 60) {
            throw new IllegalArgumentException("Логин должен быть не длиннее 60 символов");
        }
        return username;
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Пароль не должен быть пустым");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Пароль должен быть не короче 6 символов");
        }
        if (password.length() > 80) {
            throw new IllegalArgumentException("Пароль должен быть не длиннее 80 символов");
        }
    }
}