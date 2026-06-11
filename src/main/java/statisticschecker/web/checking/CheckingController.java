package statisticschecker.web.checking;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.CheckedStudent;
import statisticschecker.service.checking.CheckingService;
import statisticschecker.web.common.ResponseMapper;

@RestController
public class CheckingController {
    private final CheckingService checkingService;
    private final ResponseMapper responseMapper;

    public CheckingController(CheckingService checkingService, ResponseMapper responseMapper) {
        this.checkingService = checkingService;
        this.responseMapper = responseMapper;
    }

    @GetMapping("/api/control-works/{controlWorkId}/groups")
    public List<GroupResponse> findGroups(@PathVariable Integer controlWorkId) {
        List<StudentGroup> groups = checkingService.findGroupsByControlWork(controlWorkId);
        return responseMapper.toGroupResponseList(groups);
    }

    @GetMapping("/api/groups/{groupId}/students")
    public List<StudentResponse> findStudents(@PathVariable Integer groupId) {
        List<CheckedStudent> checkedStudents = checkingService.findStudentsByGroup(groupId);
        return responseMapper.toStudentResponseList(checkedStudents);
    }

    @GetMapping("/api/students/{studentId}")
    public StudentResponse findStudent(@PathVariable Integer studentId) {
        CheckedStudent checkedStudent = checkingService.findStudent(studentId);
        return responseMapper.toResponse(checkedStudent);
    }

    @GetMapping("/api/students/{studentId}/assignments")
    public List<CheckedAssignmentResponse> findAssignments(@PathVariable Integer studentId) {
        List<CheckedAssignment> assignments = checkingService.findAssignmentsByStudent(studentId);
        return responseMapper.toCheckedAssignmentResponseList(assignments);
    }
}
