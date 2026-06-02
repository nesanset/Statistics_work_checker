package statisticschecker.web.dto.report;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record AssignmentReportResponse(Integer assignmentId, int number, BigDecimal score, BigDecimal maxScore, CommentTemplate commentTemplate) {
}