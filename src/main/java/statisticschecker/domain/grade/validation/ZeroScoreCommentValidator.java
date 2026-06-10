package statisticschecker.domain.grade.validation;

import statisticschecker.domain.grade.CommentTemplate;

public class ZeroScoreCommentValidator extends AbstractGradeValidationHandler {

    @Override
    protected void check(GradeValidationContext context) {
        if (context.score().doubleValue() == 0 && context.commentTemplate() == CommentTemplate.COMPLETED_CORRECTLY) {
            throw new IllegalArgumentException("При нулевой оценке нельзя выбрать комментарий - \"Выполнено корректно\"");
        }
    }
}