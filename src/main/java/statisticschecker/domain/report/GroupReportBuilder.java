package statisticschecker.domain.report;

import java.math.BigDecimal;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.StudentWork;

public interface GroupReportBuilder {
    void reset();

    void buildControlWork(Long controlWorkId, String controlWorkTitle);

    void buildGroup(StudentGroup group);

    void buildPassingScore(BigDecimal passingScore);

    void buildStudentWork(StudentWork studentWork);

    GroupReport getReport();
}
