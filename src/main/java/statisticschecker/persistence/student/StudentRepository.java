package statisticschecker.persistence.student;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    List<StudentEntity> findByStudentGroupIdOrderByFullNameAsc(Integer studentGroupId);
}