package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.domain.grade.GradeChange;
import statisticschecker.web.dto.grade.GradeResponse;

@Component
public class GradeResponseMapper {

    public GradeResponse toResponse(GradeChange gradeChange) {
        return new GradeResponse(gradeChange.studentId(), gradeChange.assignmentId(), gradeChange.score(), gradeChange.commentTemplate(), gradeChange.checkStatus());
    }

    public List<GradeResponse> toResponseList(List<GradeChange> gradeChanges) {
        List<GradeResponse> responses = new ArrayList<>();
        for (GradeChange gradeChange : gradeChanges) {
            responses.add(toResponse(gradeChange));
        }
        return responses;
    }
}
