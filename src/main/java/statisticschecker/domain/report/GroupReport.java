package statisticschecker.domain.report;

import java.math.BigDecimal;
import java.util.List;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.StudentWork;
import statisticschecker.domain.validation.DomainValidation;

public record GroupReport(Integer controlWorkId, String controlWorkTitle, StudentGroup group, BigDecimal passingScore, List<StudentWork> studentWorks) {

    public GroupReport {
        studentWorks = DomainValidation.copyNullableList(studentWorks);
    }
}