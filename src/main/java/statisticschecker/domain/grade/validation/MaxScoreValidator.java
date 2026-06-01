package statisticschecker.domain.grade.validation;

public class MaxScoreValidator extends AbstractGradeValidationHandler {

    @Override
    protected void check(GradeValidationContext context) {
        if (context.score().compareTo(context.maxScore()) > 0) {
            throw new IllegalArgumentException("Оценка не должна превышать максимальный балл");
        }
    }
}