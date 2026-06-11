package statisticschecker.service.checking;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Component;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.status.*;
import statisticschecker.domain.student.*;
import statisticschecker.persistence.assignment.*;
import statisticschecker.persistence.grade.*;
import statisticschecker.persistence.student.StudentEntity;
import statisticschecker.service.common.DomainMapper;

@Component
public class StudentWorkService {
    private final AssignmentRepository assignmentRepository;
    private final GradeRepository gradeRepository;
    private final DomainMapper domainMapper;

    public StudentWorkService(AssignmentRepository assignmentRepository, GradeRepository gradeRepository, DomainMapper domainMapper) {
        this.assignmentRepository = assignmentRepository;
        this.gradeRepository = gradeRepository;
        this.domainMapper = domainMapper;
    }

    public CheckedStudent buildCheckedStudent(StudentEntity student) {
        BigDecimal totalScore = calculateTotalScore(gradeRepository.findByStudentId(student.getId()));
        return domainMapper.toCheckedStudent(student, totalScore);
    }

    public List<CheckedAssignment> buildAssignments(StudentEntity student) {
        List<AssignmentEntity> assignments = assignmentRepository.findByVariantIdOrderByNumberAsc(student.getVariant().getId());
        List<GradeEntity> grades = gradeRepository.findByStudentId(student.getId());

        List<CheckedAssignment> checkedAssignments = new ArrayList<>();
        for (AssignmentEntity assignment : assignments) {
            GradeEntity grade = findGradeForAssignment(assignment, grades);
            checkedAssignments.add(domainMapper.toCheckedAssignment(assignment, grade));
        }
        return checkedAssignments;
    }

    public StudentWork buildStudentWork(StudentEntity student, BigDecimal passingScore) {
        List<GradeEntity> grades = gradeRepository.findByStudentId(student.getId());
        CheckedStudent checkedStudent = buildCheckedStudent(student, grades);
        List<CheckedAssignment> checkedAssignments = buildAssignments(student, grades);
        CheckStatus checkStatus = checkedStudent.checkStatus();
        PassingStatus passingStatus = PassingStatus.calculate(checkedStudent.totalScore(), passingScore, checkStatus);
        return new StudentWork(checkedStudent, passingStatus, checkedAssignments);
    }

    private CheckedStudent buildCheckedStudent(StudentEntity student, List<GradeEntity> grades) {
        BigDecimal totalScore = calculateTotalScore(grades);
        return domainMapper.toCheckedStudent(student, totalScore);
    }

    private List<CheckedAssignment> buildAssignments(StudentEntity student, List<GradeEntity> grades) {
        List<AssignmentEntity> assignments = assignmentRepository.findByVariantIdOrderByNumberAsc(student.getVariant().getId());
        List<CheckedAssignment> checkedAssignments = new ArrayList<>();
        for (AssignmentEntity assignment : assignments) {
            GradeEntity grade = findGradeForAssignment(assignment, grades);
            checkedAssignments.add(domainMapper.toCheckedAssignment(assignment, grade));
        }
        return checkedAssignments;
    }

    private BigDecimal calculateTotalScore(List<GradeEntity> grades) {
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
}