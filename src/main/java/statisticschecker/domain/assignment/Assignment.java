package statisticschecker.domain.assignment;

import java.math.BigDecimal;

public record Assignment(Long id, String variantCode, int number, String text, BigDecimal maxScore) {

    public Assignment {
        if (variantCode == null || variantCode.isBlank()) {
            throw new IllegalArgumentException("Код варианта не должен быть пустым");
        }
        if (number <= 0) {
            throw new IllegalArgumentException("Номер задания должен быть положительным");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Текст задания не должен быть пустым");
        }
        if (maxScore == null || maxScore.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Максимальный балл должен быть положительным");
        }

        variantCode = variantCode.trim();
        text = text.trim();
        maxScore = maxScore.stripTrailingZeros();
    }
}