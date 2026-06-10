package statisticschecker.persistence.grade;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<GradeEntity, Integer> {
    List<GradeEntity> findByStudentId(Integer studentId);

    Optional<GradeEntity> findByStudentIdAndAssignmentId(Integer studentId, Integer assignmentId);

    void deleteByStudentIdAndAssignmentId(Integer studentId, Integer assignmentId);
}