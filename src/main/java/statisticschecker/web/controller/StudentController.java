package statisticschecker.web.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import statisticschecker.service.checking.CheckingViewService;
import statisticschecker.service.checking.StudentResult;
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
        List<StudentResult> results = checkingViewService.findStudentsByGroup(groupId);
        return studentResponseMapper.toResponseList(results);
    }

    @GetMapping("/api/students/{studentId}")
    public StudentResponse findStudent(@PathVariable Integer studentId) {
        StudentResult result = checkingViewService.findStudent(studentId);
        return studentResponseMapper.toResponse(result);
    }
}