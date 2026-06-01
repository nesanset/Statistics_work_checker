package statisticschecker.domain.grade;

public enum CommentTemplate {
    NO_COMMENT("Без комментария"),
    TASK_NOT_COMPLETED("Задание не выполнено"),
    COMPLETED_CORRECTLY("Выполнено корректно");
    private final String displayName;

    private CommentTemplate(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}