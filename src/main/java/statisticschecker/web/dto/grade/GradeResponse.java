package statisticschecker.web.dto.grade;

import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;
import statisticschecker.domain.result.CheckStatus;
import statisticschecker.service.grade.GradeResult;

public record GradeResponse(Integer studentId, Integer assignmentId, BigDecimal score, CommentTemplate commentTemplate, CheckStatus checkStatus) {

    public static GradeResponse fromResult(GradeResult result) {
        return new GradeResponse(result.studentId(), result.assignmentId(), result.score(), result.commentTemplate(), result.checkStatus());
    }
}