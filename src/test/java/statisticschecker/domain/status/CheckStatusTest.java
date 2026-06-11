package statisticschecker.domain.status;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CheckStatusTest {

    @Test
    void calculatesStatusByCheckedAssignments() {
        assertThat(CheckStatus.calculate(0, 3)).isEqualTo(CheckStatus.NOT_CHECKED);
        assertThat(CheckStatus.calculate(2, 3)).isEqualTo(CheckStatus.IN_PROGRESS);
        assertThat(CheckStatus.calculate(3, 3)).isEqualTo(CheckStatus.CHECKED);
    }
}
