package statisticschecker.service.checking;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record AssignmentResult(Integer id, int number, String text, BigDecimal maxScore, BigDecimal score, CommentTemplate commentTemplate) {
}
