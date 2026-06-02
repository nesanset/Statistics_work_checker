package statisticschecker.service.checking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statisticschecker.domain.grade.CommentTemplate;
import statisticschecker.persistence.entity.AssignmentEntity;
import statisticschecker.persistence.entity.ControlWorkEntity;
import statisticschecker.persistence.entity.GradeEntity;
import statisticschecker.persistence.entity.StudentEntity;
import statisticschecker.persistence.entity.StudentGroupEntity;
import statisticschecker.persistence.repository.AssignmentRepository;
import statisticschecker.persistence.repository.ControlWorkRepository;
import statisticschecker.persistence.repository.GradeRepository;
import statisticschecker.persistence.repository.StudentGroupRepository;
import statisticschecker.persistence.repository.StudentRepository;

@Service
public class CheckingViewService {
    private final ControlWorkRepository controlWorkRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final GradeRepository gradeRepository;

    public CheckingViewService(ControlWorkRepository controlWorkRepository,
                               StudentGroupRepository studentGroupRepository,
                               StudentRepository studentRepository,
                               AssignmentRepository assignmentRepository,
                               GradeRepository gradeRepository) {
        this.controlWorkRepository = controlWorkRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.gradeRepository = gradeRepository;
    }

    @Transactional(readOnly = true)
    public List<GroupResult> findGroupsByControlWork(Integer controlWorkId) {
        findControlWork(controlWorkId);
        List<StudentGroupEntity> groups = studentGroupRepository.findByControlWorkIdOrderByNameAsc(controlWorkId);

        List<GroupResult> results = new ArrayList<>();
        for (StudentGroupEntity group : groups) {
            results.add(new GroupResult(group.getId(), group.getName()));
        }
        return results;
    }

    @Transactional(readOnly = true)
    public List<StudentResult> findStudentsByGroup(Integer groupId) {
        findGroup(groupId);
        List<StudentEntity> students = studentRepository.findByStudentGroupIdOrderByFullNameAsc(groupId);

        List<StudentResult> results = new ArrayList<>();
        for (StudentEntity student : students) {
            results.add(buildStudentResult(student));
        }
        return results;
    }

    @Transactional(readOnly = true)
    public StudentResult findStudent(Integer studentId) {
        StudentEntity student = findStudentEntity(studentId);
        return buildStudentResult(student);
    }

    @Transactional(readOnly = true)
    public List<AssignmentResult> findAssignmentsByStudent(Integer studentId) {
        StudentEntity student = findStudentEntity(studentId);
        List<AssignmentEntity> assignments = assignmentRepository.findByVariantIdOrderByNumberAsc(student.getVariant().getId());
        List<GradeEntity> grades = gradeRepository.findByStudentId(student.getId());

        List<AssignmentResult> results = new ArrayList<>();
        for (AssignmentEntity assignment : assignments) {
            GradeEntity grade = findGradeForAssignment(assignment, grades);
            results.add(buildAssignmentResult(assignment, grade));
        }
        return results;
    }

    private ControlWorkEntity findControlWork(Integer controlWorkId) {
        if (controlWorkId == null) {
            throw new IllegalArgumentException("Идентификатор контрольной работы не должен быть пустым");
        }
        Optional<ControlWorkEntity> controlWork = controlWorkRepository.findById(controlWorkId);
        if (controlWork.isEmpty()) {
            throw new IllegalArgumentException("Контрольная работа не найдена");
        }
        return controlWork.get();
    }

    private StudentGroupEntity findGroup(Integer groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("Идентификатор группы не должен быть пустым");
        }
        Optional<StudentGroupEntity> group = studentGroupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new IllegalArgumentException("Группа не найдена");
        }
        return group.get();
    }

    private StudentEntity findStudentEntity(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Идентификатор студента не должен быть пустым");
        }
        Optional<StudentEntity> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new IllegalArgumentException("Студент не найден");
        }
        return student.get();
    }

    private StudentResult buildStudentResult(StudentEntity student) {
        BigDecimal totalScore = calculateTotalScore(student.getId());
        return new StudentResult(
                student.getId(),
                student.getStudentGroup().getId(),
                student.getFullName(),
                student.getVariant().getCode(),
                student.getCheckStatus(),
                totalScore);
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

    private AssignmentResult buildAssignmentResult(AssignmentEntity assignment, GradeEntity grade) {
        BigDecimal score = null;
        CommentTemplate commentTemplate = CommentTemplate.NO_COMMENT;
        if (grade != null) {
            score = grade.getScore();
            if (grade.getCommentTemplate() != null) {
                commentTemplate = grade.getCommentTemplate();
            }
        }
        return new AssignmentResult(assignment.getId(), assignment.getNumber(), assignment.getText(), assignment.getMaxScore(), score, commentTemplate);
    }
}
