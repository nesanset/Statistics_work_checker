package statisticschecker.domain.student;

import java.util.List;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.status.PassingStatus;
import statisticschecker.domain.validation.DomainValidation;

public record StudentWork(CheckedStudent student, PassingStatus passingStatus, List<CheckedAssignment> assignments) {

    public StudentWork {
        assignments = DomainValidation.copyNullableList(assignments);
    }
}