package statisticschecker.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import statisticschecker.persistence.entity.StudentGroupEntity;

public interface StudentGroupRepository extends JpaRepository<StudentGroupEntity, Integer> {
    List<StudentGroupEntity> findByControlWorkIdOrderByNameAsc(Integer controlWorkId);

    Optional<StudentGroupEntity> findByControlWorkIdAndName(Integer controlWorkId, String name);
}