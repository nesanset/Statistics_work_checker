package statisticschecker.domain.assignment;

import java.math.BigDecimal;
import statisticschecker.domain.validation.DomainValidation;

public record Assignment(String variantCode, int number, String text, BigDecimal maxScore) {

    public Assignment {
        variantCode = DomainValidation.requireText(variantCode, "Код варианта не должен быть пустым");
        number = DomainValidation.requirePositive(number, "Номер задания должен быть положительным");
        text = DomainValidation.requireText(text, "Текст задания не должен быть пустым");
        maxScore = DomainValidation.requirePositive(maxScore, "Максимальный балл должен быть положительным");
    }
}