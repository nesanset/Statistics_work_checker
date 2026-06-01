package statisticschecker.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import statisticschecker.persistence.entity.ControlWorkEntity;

public interface ControlWorkRepository extends JpaRepository<ControlWorkEntity, Integer> {
    List<ControlWorkEntity> findByCreatedByUserIdOrderByCreatedAtDesc(Integer createdByUserId);
}