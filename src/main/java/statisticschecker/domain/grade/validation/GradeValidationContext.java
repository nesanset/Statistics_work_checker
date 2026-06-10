package statisticschecker.domain.grade.validation;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record GradeValidationContext(BigDecimal score, BigDecimal maxScore, CommentTemplate commentTemplate) {
}