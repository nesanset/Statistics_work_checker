package statisticschecker.service.grade;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;
import statisticschecker.domain.result.CheckStatus;

public record GradeResult(Integer studentId, Integer assignmentId, BigDecimal score, CommentTemplate commentTemplate, CheckStatus checkStatus) {
}