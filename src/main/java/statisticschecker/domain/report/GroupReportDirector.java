package statisticschecker.domain.report;

import java.math.BigDecimal;
import java.util.List;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.StudentWork;

public class GroupReportDirector {
    private final GroupReportBuilder builder;

    public GroupReportDirector(GroupReportBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Строитель отчета не должен быть пустым");
        }
        this.builder = builder;
    }

    public GroupReport constructGroupReport(Long controlWorkId,
                                            String controlWorkTitle,
                                            StudentGroup group,
                                            BigDecimal passingScore,
                                            List<StudentWork> studentWorks) {
        if (studentWorks == null) {
            throw new IllegalArgumentException("Список работ студентов не должен быть пустым");
        }
        builder.reset();
        builder.buildControlWork(controlWorkId, controlWorkTitle);
        builder.buildGroup(group);
        builder.buildPassingScore(passingScore);
        for (StudentWork studentWork : studentWorks) {
            builder.buildStudentWork(studentWork);
        }
        return builder.getReport();
    }
}
