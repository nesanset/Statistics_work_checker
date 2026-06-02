package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.service.checking.StudentResult;
import statisticschecker.web.dto.student.StudentResponse;

@Component
public class StudentResponseMapper {

    public StudentResponse toResponse(StudentResult result) {
        return new StudentResponse(result.id(), result.groupId(), result.fullName(), result.variantCode(), result.checkStatus(), result.totalScore());
    }

    public List<StudentResponse> toResponseList(List<StudentResult> results) {
        List<StudentResponse> responses = new ArrayList<>();
        for (StudentResult result : results) {
            responses.add(toResponse(result));
        }
        return responses;
    }
}
