package statisticschecker.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import statisticschecker.persistence.entity.AppUserEntity;

public interface AppUserRepository extends JpaRepository<AppUserEntity, Integer> {
    Optional<AppUserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}