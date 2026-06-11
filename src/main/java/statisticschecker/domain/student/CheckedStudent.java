package statisticschecker.domain.student;

import java.math.BigDecimal;
import statisticschecker.domain.status.CheckStatus;

public record CheckedStudent(Integer id, Integer groupId, String fullName, String variantCode, CheckStatus checkStatus, BigDecimal totalScore) {

    public CheckedStudent {
        if (totalScore == null) {
            totalScore = BigDecimal.ZERO;
        }
        totalScore = totalScore.stripTrailingZeros();
    }
}