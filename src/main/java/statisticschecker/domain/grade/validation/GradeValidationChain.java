package statisticschecker.domain.grade.validation;

public class GradeValidationChain {
    private final GradeValidationHandler firstHandler;

    public GradeValidationChain(GradeValidationHandler firstHandler) {
        if (firstHandler == null) {
            throw new IllegalArgumentException("Первый обработчик проверки оценки не должен быть пустым");
        }
        this.firstHandler = firstHandler;
    }

    public static GradeValidationChain createDefault() {
        AbstractGradeValidationHandler scoreRequiredValidator = new ScoreRequiredValidator();
        AbstractGradeValidationHandler nonNegativeScoreValidator = new NonNegativeScoreValidator();
        GradeValidationHandler maxScoreValidator = new MaxScoreValidator();

        scoreRequiredValidator.setNextHandler(nonNegativeScoreValidator);
        nonNegativeScoreValidator.setNextHandler(maxScoreValidator);

        return new GradeValidationChain(scoreRequiredValidator);
    }

    public void validate(GradeValidationContext context) {
        firstHandler.validate(context);
    }
}