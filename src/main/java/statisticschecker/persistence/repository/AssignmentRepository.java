package statisticschecker.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import statisticschecker.persistence.entity.AssignmentEntity;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Integer> {
    List<AssignmentEntity> findByVariantIdOrderByNumberAsc(Integer variantId);
}