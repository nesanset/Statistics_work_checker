package statisticschecker.domain.grade.validation;

import java.math.BigDecimal;

public class NonNegativeScoreValidator extends AbstractGradeValidationHandler {

    @Override
    protected void check(GradeValidationContext context) {
        if (context.score().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Оценка не должна быть отрицательной");
        }
    }
}