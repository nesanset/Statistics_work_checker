package statisticschecker.web.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import statisticschecker.domain.student.CheckedStudent;
import statisticschecker.service.CheckingViewService;
import statisticschecker.web.dto.student.StudentResponse;
import statisticschecker.web.mapper.StudentResponseMapper;

@RestController
public class StudentController {
    private final CheckingViewService checkingViewService;
    private final StudentResponseMapper studentResponseMapper;

    public StudentController(CheckingViewService checkingViewService, StudentResponseMapper studentResponseMapper) {
        this.checkingViewService = checkingViewService;
        this.studentResponseMapper = studentResponseMapper;
    }

    @GetMapping("/api/groups/{groupId}/students")
    public List<StudentResponse> findStudents(@PathVariable Integer groupId) {
        List<CheckedStudent> checkedStudents = checkingViewService.findStudentsByGroup(groupId);
        return studentResponseMapper.toResponseList(checkedStudents);
    }

    @GetMapping("/api/students/{studentId}")
    public StudentResponse findStudent(@PathVariable Integer studentId) {
        CheckedStudent checkedStudent = checkingViewService.findStudent(studentId);
        return studentResponseMapper.toResponse(checkedStudent);
    }
}
