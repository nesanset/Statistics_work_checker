package statisticschecker.web.common;

import java.util.*;
import org.springframework.stereotype.Component;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.controlwork.ControlWork;
import statisticschecker.domain.grade.GradeChange;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.report.GroupReport;
import statisticschecker.domain.student.*;
import statisticschecker.domain.user.AppUser;
import statisticschecker.web.auth.AuthResponse;
import statisticschecker.web.checking.*;
import statisticschecker.web.controlwork.ControlWorkResponse;
import statisticschecker.web.grade.GradeResponse;
import statisticschecker.web.report.*;

@Component
public class ResponseMapper {

    public AuthResponse toResponse(AppUser user) {
        return new AuthResponse(user.id(), user.username(), user.createdAt());
    }

    public ControlWorkResponse toResponse(ControlWork controlWork) {
        return new ControlWorkResponse(controlWork.id(), controlWork.createdByUserId(), controlWork.title(), controlWork.passingScore(), controlWork.studentListFileName(), controlWork.variantsRootPath(), controlWork.createdAt());
    }

    public List<ControlWorkResponse> toControlWorkResponseList(List<ControlWork> controlWorks) {
        List<ControlWorkResponse> responses = new ArrayList<>();
        for (ControlWork controlWork : controlWorks) {
            responses.add(toResponse(controlWork));
        }
        return responses;
    }

    public GroupResponse toResponse(StudentGroup group) {
        return new GroupResponse(group.id(), group.name());
    }

    public List<GroupResponse> toGroupResponseList(List<StudentGroup> groups) {
        List<GroupResponse> responses = new ArrayList<>();
        for (StudentGroup group : groups) {
            responses.add(toResponse(group));
        }
        return responses;
    }

    public StudentResponse toResponse(CheckedStudent student) {
        return new StudentResponse(student.id(), student.groupId(), student.fullName(), student.variantCode(), student.checkStatus(), student.totalScore());
    }

    public List<StudentResponse> toStudentResponseList(List<CheckedStudent> students) {
        List<StudentResponse> responses = new ArrayList<>();
        for (CheckedStudent student : students) {
            responses.add(toResponse(student));
        }
        return responses;
    }

    public CheckedAssignmentResponse toResponse(CheckedAssignment assignment) {
        return new CheckedAssignmentResponse(assignment.id(), assignment.number(), assignment.text(), assignment.maxScore(), assignment.score(), assignment.commentTemplate());
    }

    public List<CheckedAssignmentResponse> toCheckedAssignmentResponseList(List<CheckedAssignment> assignments) {
        List<CheckedAssignmentResponse> responses = new ArrayList<>();
        for (CheckedAssignment assignment : assignments) {
            responses.add(toResponse(assignment));
        }
        return responses;
    }

    public GradeResponse toResponse(GradeChange gradeChange) {
        return new GradeResponse(gradeChange.studentId(), gradeChange.assignmentId(), gradeChange.score(), gradeChange.commentTemplate(), gradeChange.checkStatus());
    }

    public List<GradeResponse> toGradeResponseList(List<GradeChange> gradeChanges) {
        List<GradeResponse> responses = new ArrayList<>();
        for (GradeChange gradeChange : gradeChanges) {
            responses.add(toResponse(gradeChange));
        }
        return responses;
    }

    public GroupReportResponse toResponse(GroupReport report) {
        return new GroupReportResponse(report.controlWorkId(), report.controlWorkTitle(), report.group().id(), report.group().name(), report.passingScore(), toStudentReportResponseList(report.studentWorks()));
    }

    private List<StudentReportResponse> toStudentReportResponseList(List<StudentWork> studentWorks) {
        List<StudentReportResponse> responses = new ArrayList<>();
        for (StudentWork studentWork : studentWorks) {
            responses.add(toStudentReportResponse(studentWork));
        }
        return responses;
    }

    private StudentReportResponse toStudentReportResponse(StudentWork studentWork) {
        return new StudentReportResponse(studentWork.student().id(), studentWork.student().fullName(), studentWork.student().variantCode(), studentWork.student().checkStatus(), studentWork.student().totalScore(), studentWork.passingStatus(), toCheckedAssignmentResponseList(studentWork.assignments()));
    }
}