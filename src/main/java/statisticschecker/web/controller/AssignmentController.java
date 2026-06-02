package statisticschecker.web.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import statisticschecker.service.checking.AssignmentResult;
import statisticschecker.service.checking.CheckingViewService;
import statisticschecker.web.dto.assignment.AssignmentResponse;
import statisticschecker.web.mapper.AssignmentResponseMapper;

@RestController
@RequestMapping("/api/students/{studentId}/assignments")
public class AssignmentController {
    private final CheckingViewService checkingViewService;
    private final AssignmentResponseMapper assignmentResponseMapper;

    public AssignmentController(CheckingViewService checkingViewService, AssignmentResponseMapper assignmentResponseMapper) {
        this.checkingViewService = checkingViewService;
        this.assignmentResponseMapper = assignmentResponseMapper;
    }

    @GetMapping
    public List<AssignmentResponse> findAssignments(@PathVariable Integer studentId) {
        List<AssignmentResult> results = checkingViewService.findAssignmentsByStudent(studentId);
        return assignmentResponseMapper.toResponseList(results);
    }
}