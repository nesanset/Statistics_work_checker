package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.service.checking.AssignmentResult;
import statisticschecker.web.dto.assignment.AssignmentResponse;

@Component
public class AssignmentResponseMapper {

    public AssignmentResponse toResponse(AssignmentResult result) {
        return new AssignmentResponse(result.id(), result.number(), result.text(), result.maxScore(), result.score(), result.commentTemplate());
    }

    public List<AssignmentResponse> toResponseList(List<AssignmentResult> results) {
        List<AssignmentResponse> responses = new ArrayList<>();
        for (AssignmentResult result : results) {
            responses.add(toResponse(result));
        }
        return responses;
    }
}
