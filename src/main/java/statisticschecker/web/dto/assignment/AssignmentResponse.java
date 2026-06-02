package statisticschecker.web.dto.assignment;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record AssignmentResponse(Integer id, int number, String text, BigDecimal maxScore, BigDecimal score, CommentTemplate commentTemplate) {
}