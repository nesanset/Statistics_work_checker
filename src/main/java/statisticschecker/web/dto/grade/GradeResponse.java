package statisticschecker.web.dto.grade;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;
import statisticschecker.domain.status.CheckStatus;

public record GradeResponse(Integer studentId, Integer assignmentId, BigDecimal score, CommentTemplate commentTemplate, CheckStatus checkStatus) {
}
