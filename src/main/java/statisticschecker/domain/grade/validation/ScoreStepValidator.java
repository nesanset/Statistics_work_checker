package statisticschecker.domain.grade.validation;

public class ScoreStepValidator extends AbstractGradeValidationHandler {
    private static final int PARTS_IN_POINT = 4;

    @Override
    protected void check(GradeValidationContext context) {
        if (context.score().doubleValue() * PARTS_IN_POINT % 1 != 0) {
            throw new IllegalArgumentException("Оценка должна быть кратна 0.25");
        }
    }
}