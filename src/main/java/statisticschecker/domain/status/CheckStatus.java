package statisticschecker.domain.status;

public enum CheckStatus {
    NOT_CHECKED,
    IN_PROGRESS,
    CHECKED,
    MISSING_WORK;

    public static CheckStatus calculate(int checkedCount, int assignmentCount) {
        if (assignmentCount <= 0 || checkedCount <= 0) {
            return NOT_CHECKED;
        }
        if (checkedCount >= assignmentCount) {
            return CHECKED;
        }
        return IN_PROGRESS;
    }
}