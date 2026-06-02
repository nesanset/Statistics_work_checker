package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.web.dto.assignment.AssignmentResponse;

@Component
public class AssignmentResponseMapper {

    public AssignmentResponse toResponse(CheckedAssignment checkedAssignment) {
        return new AssignmentResponse(checkedAssignment.id(), checkedAssignment.number(), checkedAssignment.text(), checkedAssignment.maxScore(), checkedAssignment.score(), checkedAssignment.commentTemplate());
    }

    public List<AssignmentResponse> toResponseList(List<CheckedAssignment> checkedAssignments) {
        List<AssignmentResponse> responses = new ArrayList<>();
        for (CheckedAssignment checkedAssignment : checkedAssignments) {
            responses.add(toResponse(checkedAssignment));
        }
        return responses;
    }
}
