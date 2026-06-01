package statisticschecker.domain.grade.validation;

public abstract class AbstractGradeValidationHandler implements GradeValidationHandler {
    private GradeValidationHandler nextHandler;

    public void setNextHandler(GradeValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public final void validate(GradeValidationContext context) {
        check(context);
        if (nextHandler != null) {
            nextHandler.validate(context);
        }
    }

    protected abstract void check(GradeValidationContext context);
}