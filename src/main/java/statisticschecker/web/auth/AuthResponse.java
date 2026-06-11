package statisticschecker.web.auth;

import java.time.LocalDateTime;

public record AuthResponse(Integer id, String username, LocalDateTime createdAt) {
}
