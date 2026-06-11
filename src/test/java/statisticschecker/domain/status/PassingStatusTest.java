package statisticschecker.domain.status;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PassingStatusTest {

    @Test
    void returnsPassedWhenScoreIsEnough() {
        PassingStatus status = PassingStatus.calculate(new BigDecimal("15"), new BigDecimal("10"), CheckStatus.CHECKED);

        assertThat(status).isEqualTo(PassingStatus.PASSED);
    }
}
