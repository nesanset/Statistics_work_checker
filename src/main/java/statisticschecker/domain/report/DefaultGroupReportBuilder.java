package statisticschecker.domain.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.StudentWork;

public class DefaultGroupReportBuilder implements GroupReportBuilder {
    private Long controlWorkId;
    private String controlWorkTitle;
    private StudentGroup group;
    private BigDecimal passingScore;
    private List<StudentWork> studentWorks;

    public DefaultGroupReportBuilder() {
        reset();
    }

    @Override
    public void reset() {
        controlWorkId = null;
        controlWorkTitle = null;
        group = null;
        passingScore = null;
        studentWorks = new ArrayList<>();
    }

    @Override
    public void buildControlWork(Long controlWorkId, String controlWorkTitle) {
        this.controlWorkId = controlWorkId;
        this.controlWorkTitle = controlWorkTitle;
    }

    @Override
    public void buildGroup(StudentGroup group) {
        this.group = group;
    }

    @Override
    public void buildPassingScore(BigDecimal passingScore) {
        this.passingScore = passingScore;
    }

    @Override
    public void buildStudentWork(StudentWork studentWork) {
        if (studentWork == null) {
            throw new IllegalArgumentException("Работа студента не должна быть пустой");
        }
        studentWorks.add(studentWork);
    }

    @Override
    public GroupReport getReport() {
        return new GroupReport(controlWorkId, controlWorkTitle, group, passingScore, studentWorks);
    }
}
