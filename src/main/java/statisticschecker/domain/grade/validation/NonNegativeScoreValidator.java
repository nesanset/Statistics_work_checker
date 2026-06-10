package statisticschecker.domain.grade.validation;

public class NonNegativeScoreValidator extends AbstractGradeValidationHandler {

    @Override
    protected void check(GradeValidationContext context) {
        if (context.score().doubleValue() < 0) {
            throw new IllegalArgumentException("Оценка не должна быть отрицательной");
        }
    }
}