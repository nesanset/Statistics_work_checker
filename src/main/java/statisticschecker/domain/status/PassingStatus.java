package statisticschecker.domain.status;

import java.math.BigDecimal;

public enum PassingStatus {
    NOT_CHECKED,
    PASSED,
    FAILED;

    public static PassingStatus calculate(BigDecimal totalScore, BigDecimal passingScore, CheckStatus checkStatus) {
        if (checkStatus == CheckStatus.NOT_CHECKED || checkStatus == CheckStatus.IN_PROGRESS) {
            return NOT_CHECKED;
        }
        if (totalScore.compareTo(passingScore) >= 0) {
            return PASSED;
        }
        return FAILED;
    }
}