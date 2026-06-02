package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.domain.student.CheckedStudent;
import statisticschecker.web.dto.student.StudentResponse;

@Component
public class StudentResponseMapper {

    public StudentResponse toResponse(CheckedStudent checkedStudent) {
        return new StudentResponse(checkedStudent.id().intValue(), checkedStudent.groupId().intValue(), checkedStudent.fullName(), checkedStudent.variantCode(), checkedStudent.checkStatus(), checkedStudent.totalScore());
    }

    public List<StudentResponse> toResponseList(List<CheckedStudent> checkedStudents) {
        List<StudentResponse> responses = new ArrayList<>();
        for (CheckedStudent checkedStudent : checkedStudents) {
            responses.add(toResponse(checkedStudent));
        }
        return responses;
    }
}
