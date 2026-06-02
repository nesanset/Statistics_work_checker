package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.service.grade.GradeResult;
import statisticschecker.web.dto.grade.GradeResponse;

@Component
public class GradeResponseMapper {

    public GradeResponse toResponse(GradeResult result) {
        return new GradeResponse(result.studentId(), result.assignmentId(), result.score(), result.commentTemplate(), result.checkStatus());
    }

    public List<GradeResponse> toResponseList(List<GradeResult> results) {
        List<GradeResponse> responses = new ArrayList<>();
        for (GradeResult result : results) {
            responses.add(toResponse(result));
        }
        return responses;
    }
}
