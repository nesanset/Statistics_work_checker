package statisticschecker.domain.grade;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class GradeTest {

    @Test
    void rejectsScoreGreaterThanMaxScore() {
        Grade grade = new Grade(new BigDecimal("5"));

        assertThatThrownBy(() -> grade.updateScore(new BigDecimal("6"), CommentTemplate.NO_COMMENT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("максимальный балл");
    }

    @Test
    void rejectsZeroScoreWithCompletedComment() {
        Grade grade = new Grade(new BigDecimal("5"));

        assertThatThrownBy(() -> grade.updateScore(BigDecimal.ZERO, CommentTemplate.COMPLETED_CORRECTLY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Выполнено корректно");
    }

    @Test
    void rejectsWrongScoreStep() {
        Grade grade = new Grade(new BigDecimal("5"));

        assertThatThrownBy(() -> grade.updateScore(new BigDecimal("1.10"), CommentTemplate.NO_COMMENT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("0.25");
    }
}
