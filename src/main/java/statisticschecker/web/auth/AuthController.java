package statisticschecker.web.auth;

import org.springframework.web.bind.annotation.*;
import statisticschecker.domain.user.AppUser;
import statisticschecker.service.auth.AuthService;
import statisticschecker.web.common.ResponseMapper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final ResponseMapper responseMapper;

    public AuthController(AuthService authService, ResponseMapper responseMapper) {
        this.authService = authService;
        this.responseMapper = responseMapper;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestParam String username, @RequestParam String password) {
        AppUser user = authService.register(username, password);
        return responseMapper.toResponse(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestParam String username, @RequestParam String password) {
        AppUser user = authService.login(username, password);
        return responseMapper.toResponse(user);
    }
}