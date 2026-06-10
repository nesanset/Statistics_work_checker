package statisticschecker.persistence.variant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<VariantEntity, Integer> {
    boolean existsByControlWorkId(Integer controlWorkId);
}