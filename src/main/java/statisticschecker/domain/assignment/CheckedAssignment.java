package statisticschecker.domain.assignment;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;
import statisticschecker.domain.validation.DomainValidation;

public record CheckedAssignment(Integer id, String variantCode, int number, String text, BigDecimal maxScore, BigDecimal score, CommentTemplate commentTemplate) {

    public CheckedAssignment {
        id = DomainValidation.requireNotNull(id, "Идентификатор задания не должен быть пустым");
        variantCode = DomainValidation.requireText(variantCode, "Код варианта не должен быть пустым");
        number = DomainValidation.requirePositive(number, "Номер задания должен быть положительным");
        text = DomainValidation.requireText(text, "Текст задания не должен быть пустым");
        maxScore = DomainValidation.requirePositive(maxScore, "Максимальный балл должен быть положительным");
        score = DomainValidation.normalizeNullableNonNegative(score, "Балл не должен быть отрицательным");
        if (commentTemplate == null) {
            commentTemplate = CommentTemplate.NO_COMMENT;
        }
    }
}
