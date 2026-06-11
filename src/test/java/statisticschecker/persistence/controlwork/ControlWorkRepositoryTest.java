package statisticschecker.persistence.controlwork;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import statisticschecker.persistence.user.AppUserEntity;
import statisticschecker.persistence.user.AppUserRepository;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ControlWorkRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ControlWorkRepository controlWorkRepository;

    @Test
    void findsControlWorksByUser() {
        AppUserEntity user = appUserRepository.save(new AppUserEntity("teacher", "hash"));
        controlWorkRepository.save(new ControlWorkEntity(user, "Контрольная", new BigDecimal("10"), null, null));

        List<ControlWorkEntity> controlWorks = controlWorkRepository.findByCreatedByUserIdOrderByCreatedAtDesc(user.getId());

        assertThat(controlWorks).hasSize(1);
        assertThat(controlWorks.get(0).getTitle()).isEqualTo("Контрольная");
    }
}
