package statisticschecker.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.grade.CommentTemplate;
import statisticschecker.domain.status.CheckStatus;
import statisticschecker.domain.status.PassingStatus;
import statisticschecker.domain.student.CheckedStudent;
import statisticschecker.domain.student.StudentWork;
import statisticschecker.persistence.entity.AssignmentEntity;
import statisticschecker.persistence.entity.GradeEntity;
import statisticschecker.persistence.entity.StudentEntity;
import statisticschecker.persistence.repository.AssignmentRepository;
import statisticschecker.persistence.repository.GradeRepository;

@Component
public class StudentWorkAssembler {
    private final AssignmentRepository assignmentRepository;
    private final GradeRepository gradeRepository;

    public StudentWorkAssembler(AssignmentRepository assignmentRepository, GradeRepository gradeRepository) {
        this.assignmentRepository = assignmentRepository;
        this.gradeRepository = gradeRepository;
    }

    public CheckedStudent buildCheckedStudent(StudentEntity student) {
        BigDecimal totalScore = calculateTotalScore(student.getId());
        return new CheckedStudent(student.getId().longValue(), student.getStudentGroup().getId().longValue(), student.getFullName(), student.getVariant().getCode(), student.getCheckStatus(), totalScore);
    }

    public List<CheckedAssignment> buildCheckedAssignments(StudentEntity student) {
        List<AssignmentEntity> assignments = assignmentRepository.findByVariantIdOrderByNumberAsc(student.getVariant().getId());
        List<GradeEntity> grades = gradeRepository.findByStudentId(student.getId());

        List<CheckedAssignment> checkedAssignments = new ArrayList<>();
        for (AssignmentEntity assignment : assignments) {
            GradeEntity grade = findGradeForAssignment(assignment, grades);
            checkedAssignments.add(buildCheckedAssignment(assignment, grade));
        }
        return checkedAssignments;
    }

    public StudentWork buildStudentWork(StudentEntity student, BigDecimal passingScore) {
        CheckedStudent checkedStudent = buildCheckedStudent(student);
        List<CheckedAssignment> checkedAssignments = buildCheckedAssignments(student);
        CheckStatus checkStatus = checkedStudent.checkStatus();
        PassingStatus passingStatus = PassingStatus.calculate(checkedStudent.totalScore(), passingScore, checkStatus);
        return new StudentWork(checkedStudent, passingStatus, checkedAssignments);
    }

    private BigDecimal calculateTotalScore(Integer studentId) {
        List<GradeEntity> grades = gradeRepository.findByStudentId(studentId);
        BigDecimal totalScore = BigDecimal.ZERO;
        for (GradeEntity grade : grades) {
            if (grade.getScore() != null) {
                totalScore = totalScore.add(grade.getScore());
            }
        }
        return totalScore.stripTrailingZeros();
    }

    private GradeEntity findGradeForAssignment(AssignmentEntity assignment, List<GradeEntity> grades) {
        for (GradeEntity grade : grades) {
            if (grade.getAssignment().getId().equals(assignment.getId())) {
                return grade;
            }
        }
        return null;
    }

    private CheckedAssignment buildCheckedAssignment(AssignmentEntity assignment, GradeEntity grade) {
        BigDecimal score = null;
        CommentTemplate commentTemplate = CommentTemplate.NO_COMMENT;
        if (grade != null) {
            score = grade.getScore();
            if (grade.getCommentTemplate() != null) {
                commentTemplate = grade.getCommentTemplate();
            }
        }
        return new CheckedAssignment(assignment.getId(), assignment.getNumber(), assignment.getText(), assignment.getMaxScore(), score, commentTemplate);
    }
}