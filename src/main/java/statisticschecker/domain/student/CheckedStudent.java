package statisticschecker.domain.student;

import java.math.BigDecimal;
import statisticschecker.domain.status.CheckStatus;

public record CheckedStudent(Long id, Long groupId, String fullName, String variantCode, CheckStatus checkStatus, BigDecimal totalScore) {

    public CheckedStudent {
        if (groupId == null) {
            throw new IllegalArgumentException("Идентификатор группы не должен быть пустым");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("ФИО студента не должно быть пустым");
        }
        if (variantCode == null || variantCode.isBlank()) {
            throw new IllegalArgumentException("Код варианта не должен быть пустым");
        }
        if (checkStatus == null) {
            throw new IllegalArgumentException("Статус проверки не должен быть пустым");
        }
        if (totalScore == null) {
            totalScore = BigDecimal.ZERO;
        }
        fullName = fullName.trim();
        variantCode = variantCode.trim();
        totalScore = totalScore.stripTrailingZeros();
    }
}
