package statisticschecker.domain.user;

import java.time.LocalDateTime;

public record AppUser(Integer id, String username, LocalDateTime createdAt) {
}