package statisticschecker.domain.report;

import java.math.BigDecimal;
import java.util.List;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.StudentWork;
import statisticschecker.domain.validation.DomainValidation;

public record GroupReport(Integer controlWorkId, String controlWorkTitle, StudentGroup group, BigDecimal passingScore, List<StudentWork> studentWorks) {

    public GroupReport {
        controlWorkId = DomainValidation.requireNotNull(controlWorkId, "Идентификатор контрольной работы не должен быть пустым");
        controlWorkTitle = DomainValidation.requireText(controlWorkTitle, "Название контрольной работы не должно быть пустым");
        group = DomainValidation.requireNotNull(group, "Группа не должна быть пустой");
        passingScore = DomainValidation.requireNonNegative(passingScore, "Проходной балл не должен быть отрицательным");
        studentWorks = DomainValidation.copyNullableList(studentWorks);
    }
}