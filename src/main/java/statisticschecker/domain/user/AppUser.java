package statisticschecker.domain.user;

import java.time.LocalDateTime;
import statisticschecker.domain.validation.DomainValidation;

public record AppUser(Integer id, String username, LocalDateTime createdAt) {

    public AppUser {
        id = DomainValidation.requireNotNull(id, "Идентификатор пользователя не должен быть пустым");
        username = DomainValidation.requireText(username, "Логин пользователя не должен быть пустым");
        createdAt = DomainValidation.requireNotNull(createdAt, "Дата создания пользователя не должна быть пустой");
    }
}