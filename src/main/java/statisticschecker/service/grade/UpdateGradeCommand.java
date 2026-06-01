package statisticschecker.service.grade;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record UpdateGradeCommand(Integer studentId, Integer assignmentId, BigDecimal score, CommentTemplate commentTemplate) {
}