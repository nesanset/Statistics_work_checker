package statisticschecker.domain.student;

import java.util.ArrayList;
import java.util.List;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.status.PassingStatus;

public record StudentWork(CheckedStudent student, PassingStatus passingStatus, List<CheckedAssignment> assignments) {

    public StudentWork {
        if (student == null) {
            throw new IllegalArgumentException("Студент не должен быть пустым");
        }
        if (passingStatus == null) {
            throw new IllegalArgumentException("Статус прохождения не должен быть пустым");
        }
        if (assignments == null) {
            assignments = new ArrayList<>();
        }
        assignments = List.copyOf(assignments);
    }
}
