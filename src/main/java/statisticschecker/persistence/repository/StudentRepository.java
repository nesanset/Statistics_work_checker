package statisticschecker.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import statisticschecker.domain.status.CheckStatus;
import statisticschecker.persistence.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    List<StudentEntity> findByStudentGroupIdOrderByFullNameAsc(Integer studentGroupId);

    List<StudentEntity> findByVariantId(Integer variantId);

    List<StudentEntity> findByCheckStatus(CheckStatus checkStatus);
}
