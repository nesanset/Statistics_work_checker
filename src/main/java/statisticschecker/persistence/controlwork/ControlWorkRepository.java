package statisticschecker.persistence.controlwork;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControlWorkRepository extends JpaRepository<ControlWorkEntity, Integer> {
    List<ControlWorkEntity> findByCreatedByUserIdOrderByCreatedAtDesc(Integer createdByUserId);
}