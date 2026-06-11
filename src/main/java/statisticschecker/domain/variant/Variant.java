package statisticschecker.domain.variant;

import java.util.List;
import statisticschecker.domain.assignment.Assignment;
import statisticschecker.domain.validation.DomainValidation;

public record Variant(String code, String sourceFileName, List<Assignment> assignments) {

    public Variant {
        code = DomainValidation.requireText(code, "Код варианта не должен быть пустым");
        sourceFileName = DomainValidation.trimNullableText(sourceFileName);
        assignments = DomainValidation.requireNotEmptyList(assignments, "Список заданий не должен быть пустым");
    }
}