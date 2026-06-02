package statisticschecker.domain.assignment;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record CheckedAssignment(Integer id, int number, String text, BigDecimal maxScore, BigDecimal score, CommentTemplate commentTemplate) {
}
