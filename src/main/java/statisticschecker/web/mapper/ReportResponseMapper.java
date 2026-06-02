package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.report.GroupReport;
import statisticschecker.domain.student.StudentWork;
import statisticschecker.web.dto.report.AssignmentReportResponse;
import statisticschecker.web.dto.report.GroupReportResponse;
import statisticschecker.web.dto.report.StudentReportResponse;

@Component
public class ReportResponseMapper {

    public GroupReportResponse toResponse(GroupReport report) {
        return new GroupReportResponse(report.controlWorkId().intValue(), report.controlWorkTitle(), report.group().id().intValue(), report.group().name(), report.passingScore(), toStudentResponseList(report.studentWorks()));
    }

    private List<StudentReportResponse> toStudentResponseList(List<StudentWork> studentWorks) {
        List<StudentReportResponse> responses = new ArrayList<>();
        for (StudentWork studentWork : studentWorks) {
            responses.add(toStudentResponse(studentWork));
        }
        return responses;
    }

    private StudentReportResponse toStudentResponse(StudentWork studentWork) {
        return new StudentReportResponse(
                studentWork.student().id().intValue(),
                studentWork.student().fullName(),
                studentWork.student().variantCode(),
                studentWork.student().checkStatus(),
                studentWork.student().totalScore(),
                studentWork.passingStatus(),
                toAssignmentResponseList(studentWork.assignments()));
    }

    private List<AssignmentReportResponse> toAssignmentResponseList(List<CheckedAssignment> checkedAssignments) {
        List<AssignmentReportResponse> responses = new ArrayList<>();
        for (CheckedAssignment checkedAssignment : checkedAssignments) {
            responses.add(toAssignmentResponse(checkedAssignment));
        }
        return responses;
    }

    private AssignmentReportResponse toAssignmentResponse(CheckedAssignment checkedAssignment) {
        return new AssignmentReportResponse(checkedAssignment.id(), checkedAssignment.number(), checkedAssignment.score(), checkedAssignment.maxScore(), checkedAssignment.commentTemplate());
    }
}
