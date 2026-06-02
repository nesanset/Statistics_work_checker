package statisticschecker.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statisticschecker.domain.grade.CommentTemplate;
import statisticschecker.domain.grade.Grade;
import statisticschecker.domain.grade.GradeChange;
import statisticschecker.domain.status.CheckStatus;
import statisticschecker.persistence.entity.AssignmentEntity;
import statisticschecker.persistence.entity.GradeEntity;
import statisticschecker.persistence.entity.StudentEntity;
import statisticschecker.persistence.repository.AssignmentRepository;
import statisticschecker.persistence.repository.GradeRepository;
import statisticschecker.persistence.repository.StudentRepository;

@Service
public class GradeService {
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final GradeRepository gradeRepository;

    public GradeService(StudentRepository studentRepository, AssignmentRepository assignmentRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.gradeRepository = gradeRepository;
    }

    @Transactional
    public GradeChange updateGrade(Integer studentId, Integer assignmentId, BigDecimal score, CommentTemplate commentTemplate) {
        StudentEntity student = findStudent(studentId);
        AssignmentEntity assignment = findAssignment(assignmentId);
        validateAssignmentBelongsToStudentVariant(student, assignment);

        Grade checkedGrade = new Grade(student.getId().longValue(), assignment.getId().longValue(), assignment.getMaxScore());
        checkedGrade.updateScore(score, commentTemplate);

        GradeEntity gradeEntity = findOrCreateGrade(student, assignment);
        gradeEntity.updateScore(checkedGrade.getScore(), checkedGrade.getCommentTemplate());
        GradeEntity savedGrade = gradeRepository.save(gradeEntity);

        updateStudentStatusAfterGradeChange(student);
        return buildGradeChange(student, assignment, savedGrade);
    }

    @Transactional
    public void deleteGrade(Integer studentId, Integer assignmentId) {
        StudentEntity student = findStudent(studentId);
        AssignmentEntity assignment = findAssignment(assignmentId);
        validateAssignmentBelongsToStudentVariant(student, assignment);

        gradeRepository.deleteByStudentIdAndAssignmentId(studentId, assignmentId);
        updateStudentStatusAfterGradeChange(student);
    }

    @Transactional
    public List<GradeChange> markMissingWork(Integer studentId) {
        StudentEntity student = findStudent(studentId);
        List<AssignmentEntity> assignments = assignmentRepository.findByVariantIdOrderByNumberAsc(student.getVariant().getId());
        if (assignments.isEmpty()) {
            throw new IllegalArgumentException("У варианта студента нет заданий");
        }

        student.updateCheckStatus(CheckStatus.MISSING_WORK);
        List<GradeChange> results = new ArrayList<>();
        for (AssignmentEntity assignment : assignments) {
            Grade checkedGrade = new Grade(student.getId().longValue(), assignment.getId().longValue(), assignment.getMaxScore());
            checkedGrade.markMissingWork();

            GradeEntity gradeEntity = findOrCreateGrade(student, assignment);
            gradeEntity.updateScore(checkedGrade.getScore(), checkedGrade.getCommentTemplate());
            GradeEntity savedGrade = gradeRepository.save(gradeEntity);
            results.add(buildGradeChange(student, assignment, savedGrade));
        }

        studentRepository.save(student);
        return results;
    }

    private StudentEntity findStudent(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Идентификатор студента не должен быть пустым");
        }
        Optional<StudentEntity> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new IllegalArgumentException("Студент не найден");
        }
        return student.get();
    }

    private AssignmentEntity findAssignment(Integer assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("Идентификатор задания не должен быть пустым");
        }
        Optional<AssignmentEntity> assignment = assignmentRepository.findById(assignmentId);
        if (assignment.isEmpty()) {
            throw new IllegalArgumentException("Задание не найдено");
        }
        return assignment.get();
    }

    private void validateAssignmentBelongsToStudentVariant(StudentEntity student, AssignmentEntity assignment) {
        Integer studentVariantId = student.getVariant().getId();
        Integer assignmentVariantId = assignment.getVariant().getId();
        if (!studentVariantId.equals(assignmentVariantId)) {
            throw new IllegalArgumentException("Задание не относится к варианту выбранного студента");
        }
    }

    private GradeEntity findOrCreateGrade(StudentEntity student, AssignmentEntity assignment) {
        Optional<GradeEntity> grade = gradeRepository.findByStudentIdAndAssignmentId(student.getId(), assignment.getId());
        if (grade.isPresent()) {
            return grade.get();
        }
        return new GradeEntity(student, assignment, null, CommentTemplate.NO_COMMENT);
    }

    private void updateStudentStatusAfterGradeChange(StudentEntity student) {
        List<AssignmentEntity> assignments = assignmentRepository.findByVariantIdOrderByNumberAsc(student.getVariant().getId());
        List<GradeEntity> grades = gradeRepository.findByStudentId(student.getId());

        int checkedCount = 0;
        for (AssignmentEntity assignment : assignments) {
            if (hasCheckedGrade(assignment, grades)) {
                checkedCount++;
            }
        }

        if (checkedCount == 0) {
            student.updateCheckStatus(CheckStatus.NOT_CHECKED);
        } else if (checkedCount == assignments.size()) {
            student.updateCheckStatus(CheckStatus.CHECKED);
        } else {
            student.updateCheckStatus(CheckStatus.IN_PROGRESS);
        }
        studentRepository.save(student);
    }

    private boolean hasCheckedGrade(AssignmentEntity assignment, List<GradeEntity> grades) {
        for (GradeEntity grade : grades) {
            if (grade.getAssignment().getId().equals(assignment.getId()) && grade.getScore() != null) {
                return true;
            }
        }
        return false;
    }

    private GradeChange buildGradeChange(StudentEntity student, AssignmentEntity assignment, GradeEntity grade) {
        return new GradeChange(student.getId(), assignment.getId(), grade.getScore(), grade.getCommentTemplate(), student.getCheckStatus());
    }
}