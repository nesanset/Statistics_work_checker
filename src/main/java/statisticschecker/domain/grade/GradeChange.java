package statisticschecker.domain.grade;

import java.math.BigDecimal;
import statisticschecker.domain.status.CheckStatus;

public record GradeChange(Integer studentId, Integer assignmentId, BigDecimal score, CommentTemplate commentTemplate, CheckStatus checkStatus) {
}