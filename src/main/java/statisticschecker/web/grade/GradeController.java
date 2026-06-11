package statisticschecker.web.grade;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import statisticschecker.domain.grade.*;
import statisticschecker.service.grade.GradeService;
import statisticschecker.web.common.ResponseMapper;

@RestController
@RequestMapping("/api/students/{studentId}")
public class GradeController {
    private final GradeService gradeService;
    private final ResponseMapper responseMapper;

    public GradeController(GradeService gradeService, ResponseMapper responseMapper) {
        this.gradeService = gradeService;
        this.responseMapper = responseMapper;
    }

    @PutMapping("/assignments/{assignmentId}/grade")
    public GradeResponse updateGrade(@PathVariable Integer studentId, @PathVariable Integer assignmentId, @RequestParam BigDecimal score, @RequestParam(required = false) CommentTemplate commentTemplate) {
        GradeChange gradeChange = gradeService.updateGrade(studentId, assignmentId, score, commentTemplate);
        return responseMapper.toResponse(gradeChange);
    }

    @DeleteMapping("/assignments/{assignmentId}/grade")
    public ResponseEntity<Void> deleteGrade(@PathVariable Integer studentId, @PathVariable Integer assignmentId) {
        gradeService.deleteGrade(studentId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/missing-work")
    public List<GradeResponse> markMissingWork(@PathVariable Integer studentId) {
        List<GradeChange> gradeChanges = gradeService.markMissingWork(studentId);
        return responseMapper.toGradeResponseList(gradeChanges);
    }
}