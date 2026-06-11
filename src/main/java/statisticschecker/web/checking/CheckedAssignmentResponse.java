package statisticschecker.web.checking;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record CheckedAssignmentResponse(Integer id, int number, String text, BigDecimal maxScore, BigDecimal score, CommentTemplate commentTemplate) {
}
