package statisticschecker.domain.variant;

import statisticschecker.domain.assignment.Assignment;
import java.math.BigDecimal;
import java.util.List;

public record Variant(String code, List<Assignment> assignments) {

    public Variant {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Код варианта не должен быть пустым");
        }
        if (assignments == null) {
            throw new IllegalArgumentException("Список заданий не должен быть пустым");
        }
        code = code.trim();
        assignments = List.copyOf(assignments);
    }

    public BigDecimal calculateMaxTotalScore() {
        BigDecimal totalScore = BigDecimal.ZERO;
        for (Assignment assignment : assignments) {
            totalScore = totalScore.add(assignment.maxScore());
        }
        return totalScore.stripTrailingZeros();
    }
}