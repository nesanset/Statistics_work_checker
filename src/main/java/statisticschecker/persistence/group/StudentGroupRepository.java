package statisticschecker.persistence.group;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentGroupRepository extends JpaRepository<StudentGroupEntity, Integer> {
    List<StudentGroupEntity> findByControlWorkIdOrderByNameAsc(Integer controlWorkId);

    boolean existsByControlWorkId(Integer controlWorkId);
}