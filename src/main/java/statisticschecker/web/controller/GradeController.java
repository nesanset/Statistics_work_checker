package statisticschecker.web.controller;

import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import statisticschecker.service.grade.*t;

@RestController
@RequestMapping("/api/students/{studentId}")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PutMapping("/assignments/{assignmentId}/grade")
    public GradeResponse updateGrade(@PathVariable Integer studentId, @PathVariable Integer assignmentId, @Valid @RequestBody UpdateGradeRequest request) {
        UpdateGradeCommand command = new UpdateGradeCommand(studentId, assignmentId, request.score(), request.commentTemplate());
        GradeResult result = gradeService.updateGrade(command);
        return GradeResponse.fromResult(result);
    }

    @DeleteMapping("/assignments/{assignmentId}/grade")
    public ResponseEntity<Void> deleteGrade(@PathVariable Integer studentId, @PathVariable Integer assignmentId) {
        gradeService.deleteGrade(studentId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/missing-work")
    public List<GradeResponse> markMissingWork(@PathVariable Integer studentId) {
        List<GradeResult> results = gradeService.markMissingWork(studentId);
        List<GradeResponse> responses = new ArrayList<>();
        for (GradeResult result : results) {
            responses.add(GradeResponse.fromResult(result));
        }
        return responses;
    }
}