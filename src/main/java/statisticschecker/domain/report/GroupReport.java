package statisticschecker.domain.report;

import java.math.BigDecimal;
import java.util.List;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.StudentWork;

public record GroupReport(Long controlWorkId, String controlWorkTitle, StudentGroup group, BigDecimal passingScore, List<StudentWork> studentWorks) {

    public GroupReport {
        if (controlWorkId == null) {
            throw new IllegalArgumentException("Идентификатор контрольной работы не должен быть пустым");
        }
        if (controlWorkTitle == null || controlWorkTitle.isBlank()) {
            throw new IllegalArgumentException("Название контрольной работы не должно быть пустым");
        }
        if (group == null) {
            throw new IllegalArgumentException("Группа не должна быть пустой");
        }
        if (passingScore == null || passingScore.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Проходной балл не должен быть отрицательным");
        }
        if (studentWorks == null) {
            studentWorks = List.of();
        }
        controlWorkTitle = controlWorkTitle.trim();
        passingScore = passingScore.stripTrailingZeros();
        studentWorks = List.copyOf(studentWorks);
    }
}
