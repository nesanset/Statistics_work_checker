package statisticschecker.domain.student;

import java.util.List;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.status.PassingStatus;
import statisticschecker.domain.validation.DomainValidation;

public record StudentWork(CheckedStudent student, PassingStatus passingStatus, List<CheckedAssignment> assignments) {

    public StudentWork {
        student = DomainValidation.requireNotNull(student, "Студент не должен быть пустым");
        passingStatus = DomainValidation.requireNotNull(passingStatus, "Статус прохождения не должен быть пустым");
        assignments = DomainValidation.copyNullableList(assignments);
    }
}