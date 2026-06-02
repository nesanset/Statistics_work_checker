package statisticschecker.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import statisticschecker.service.grade.*;
import statisticschecker.web.dto.grade.*;
import statisticschecker.web.mapper.GradeResponseMapper;

@RestController
@RequestMapping("/api/students/{studentId}")
public class GradeController {
    private final GradeService gradeService;
    private final GradeResponseMapper gradeResponseMapper;

    public GradeController(GradeService gradeService, GradeResponseMapper gradeResponseMapper) {
        this.gradeService = gradeService;
        this.gradeResponseMapper = gradeResponseMapper;
    }

    @PutMapping("/assignments/{assignmentId}/grade")
    public GradeResponse updateGrade(@PathVariable Integer studentId, @PathVariable Integer assignmentId, @Valid @RequestBody UpdateGradeRequest request) {
        UpdateGradeCommand command = new UpdateGradeCommand(studentId, assignmentId, request.score(), request.commentTemplate());
        GradeResult result = gradeService.updateGrade(command);
        return gradeResponseMapper.toResponse(result);
    }

    @DeleteMapping("/assignments/{assignmentId}/grade")
    public ResponseEntity<Void> deleteGrade(@PathVariable Integer studentId, @PathVariable Integer assignmentId) {
        gradeService.deleteGrade(studentId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/missing-work")
    public List<GradeResponse> markMissingWork(@PathVariable Integer studentId) {
        List<GradeResult> results = gradeService.markMissingWork(studentId);
        return gradeResponseMapper.toResponseList(results);
    }
}