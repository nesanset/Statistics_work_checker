package statisticschecker.domain.grade;

import java.math.BigDecimal;
import statisticschecker.domain.grade.validation.GradeValidationChain;
import statisticschecker.domain.grade.validation.GradeValidationContext;
import statisticschecker.domain.validation.DomainValidation;

public class Grade {
    private static final GradeValidationChain VALIDATION_CHAIN = GradeValidationChain.createDefault();

    private final BigDecimal maxScore;
    private BigDecimal score;
    private CommentTemplate commentTemplate;

    public Grade(BigDecimal maxScore) {
        this.maxScore = DomainValidation.requirePositive(maxScore, "Максимальный балл должен быть положительным");
        this.commentTemplate = CommentTemplate.NO_COMMENT;
    }

    public BigDecimal getScore() {
        return score;
    }

    public CommentTemplate getCommentTemplate() {
        return commentTemplate;
    }

    public void updateScore(BigDecimal newScore, CommentTemplate newCommentTemplate) {
        validateScore(newScore, newCommentTemplate);
        score = newScore.stripTrailingZeros();
        if (newCommentTemplate == null) {
            commentTemplate = CommentTemplate.NO_COMMENT;
        } else {
            commentTemplate = newCommentTemplate;
        }
    }

    public void markMissingWork() {
        score = BigDecimal.ZERO;
        commentTemplate = CommentTemplate.TASK_NOT_COMPLETED;
    }

    private void validateScore(BigDecimal checkedScore, CommentTemplate checkedCommentTemplate) {
        VALIDATION_CHAIN.validate(new GradeValidationContext(checkedScore, maxScore, checkedCommentTemplate));
    }
}
