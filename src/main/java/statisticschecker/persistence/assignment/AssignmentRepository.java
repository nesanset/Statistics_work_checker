package statisticschecker.persistence.assignment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Integer> {
    List<AssignmentEntity> findByVariantIdOrderByNumberAsc(Integer variantId);
}