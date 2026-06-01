package statisticschecker.domain.grade.validation;

import java.math.BigDecimal;

public record GradeValidationContext(BigDecimal score, BigDecimal maxScore) {
}