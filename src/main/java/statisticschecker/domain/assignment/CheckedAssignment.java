package statisticschecker.domain.assignment;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record CheckedAssignment(Integer id, String variantCode, int number, String text, BigDecimal maxScore, BigDecimal score, CommentTemplate commentTemplate) {

    public CheckedAssignment {
        if (score != null) {
            score = score.stripTrailingZeros();
        }
        maxScore = maxScore.stripTrailingZeros();
        if (commentTemplate == null) {
            commentTemplate = CommentTemplate.NO_COMMENT;
        }
    }
}