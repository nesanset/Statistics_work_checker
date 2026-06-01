package statisticschecker.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import statisticschecker.persistence.entity.VariantEntity;

public interface VariantRepository extends JpaRepository<VariantEntity, Integer> {
    List<VariantEntity> findByControlWorkIdOrderByCodeAsc(Integer controlWorkId);

    Optional<VariantEntity> findByControlWorkIdAndCode(Integer controlWorkId, String code);
}