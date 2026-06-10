package statisticschecker.domain.controlwork;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import statisticschecker.domain.validation.DomainValidation;

public record ControlWork(Integer id, Integer createdByUserId, String title, BigDecimal passingScore, String studentListFileName, String variantsRootPath, LocalDateTime createdAt) {

    public ControlWork {
        id = DomainValidation.requireNotNull(id, "Идентификатор контрольной работы не должен быть пустым");
        createdByUserId = DomainValidation.requireNotNull(createdByUserId, "Идентификатор пользователя не должен быть пустым");
        title = DomainValidation.requireText(title, "Название контрольной работы не должно быть пустым");
        passingScore = DomainValidation.requireNonNegative(passingScore, "Проходной балл не должен быть отрицательным");
        studentListFileName = DomainValidation.trimNullableText(studentListFileName);
        variantsRootPath = DomainValidation.trimNullableText(variantsRootPath);
        createdAt = DomainValidation.requireNotNull(createdAt, "Дата создания контрольной работы не должна быть пустой");
    }
}