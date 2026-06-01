package statisticschecker.domain.grade;

import statisticschecker.domain.grade.validation.*;
import java.math.BigDecimal;

public class Grade {
    private final Long studentId;
    private final Long assignmentId;
    private final BigDecimal maxScore;
    private final GradeValidationChain validationChain;
    private BigDecimal score;
    private CommentTemplate commentTemplate;

    public Grade(Long studentId, Long assignmentId, BigDecimal maxScore) {
        if (studentId == null) {
            throw new IllegalArgumentException("Идентификатор студента не должен быть пустым");
        }
        if (assignmentId == null) {
            throw new IllegalArgumentException("Идентификатор задания не должен быть пустым");
        }
        if (maxScore == null || maxScore.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Максимальный балл должен быть положительным");
        }
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.maxScore = maxScore.stripTrailingZeros();
        this.validationChain = GradeValidationChain.createDefault();
        this.commentTemplate = CommentTemplate.NO_COMMENT;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public BigDecimal getScore() {
        return score;
    }

    public CommentTemplate getCommentTemplate() {
        return commentTemplate;
    }

    public boolean isChecked() {
        return score != null;
    }

    public void updateScore(BigDecimal newScore, CommentTemplate newCommentTemplate) {
        validateScore(newScore);
        score = newScore.stripTrailingZeros();
        if (newCommentTemplate == null) {
            commentTemplate = CommentTemplate.NO_COMMENT;
        } else {
            commentTemplate = newCommentTemplate;
        }
    }

    public void deleteScore() {
        score = null;
        commentTemplate = CommentTemplate.NO_COMMENT;
    }

    public void markMissingWork() {
        score = BigDecimal.ZERO;
        commentTemplate = CommentTemplate.TASK_NOT_COMPLETED;
    }

    private void validateScore(BigDecimal checkedScore) {
        validationChain.validate(new GradeValidationContext(checkedScore, maxScore));
    }
}