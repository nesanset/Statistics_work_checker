package statisticschecker.domain.grade.validation;

public class ScoreRequiredValidator extends AbstractGradeValidationHandler {

    @Override
    protected void check(GradeValidationContext context) {
        if (context.score() == null) {
            throw new IllegalArgumentException("Оценка не должна быть пустой");
        }
    }
}