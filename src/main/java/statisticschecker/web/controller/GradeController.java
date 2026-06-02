package statisticschecker.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import statisticschecker.domain.grade.GradeChange;
import statisticschecker.service.GradeService;
import statisticschecker.web.dto.grade.GradeResponse;
import statisticschecker.web.dto.grade.UpdateGradeRequest;
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
        GradeChange gradeChange = gradeService.updateGrade(studentId, assignmentId, request.score(), request.commentTemplate());
        return gradeResponseMapper.toResponse(gradeChange);
    }

    @DeleteMapping("/assignments/{assignmentId}/grade")
    public ResponseEntity<Void> deleteGrade(@PathVariable Integer studentId, @PathVariable Integer assignmentId) {
        gradeService.deleteGrade(studentId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/missing-work")
    public List<GradeResponse> markMissingWork(@PathVariable Integer studentId) {
        List<GradeChange> gradeChanges = gradeService.markMissingWork(studentId);
        return gradeResponseMapper.toResponseList(gradeChanges);
    }
}
