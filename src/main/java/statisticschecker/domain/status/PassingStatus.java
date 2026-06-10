package statisticschecker.domain.status;

import java.math.BigDecimal;
import statisticschecker.domain.validation.DomainValidation;

public enum PassingStatus {
    NOT_CHECKED,
    PASSED,
    FAILED;

    public static PassingStatus calculate(BigDecimal totalScore, BigDecimal passingScore, CheckStatus checkStatus) {
        if (checkStatus == CheckStatus.NOT_CHECKED || checkStatus == CheckStatus.IN_PROGRESS) {
            return NOT_CHECKED;
        }
        totalScore = DomainValidation.requireNotNull(totalScore, "Баллы не должны быть пустыми");
        passingScore = DomainValidation.requireNotNull(passingScore, "Баллы не должны быть пустыми");
        if (totalScore.compareTo(passingScore) >= 0) {
            return PASSED;
        }
        return FAILED;
    }
}