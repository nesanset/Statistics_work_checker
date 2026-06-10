package statisticschecker.domain.student;

import java.math.BigDecimal;
import statisticschecker.domain.status.CheckStatus;
import statisticschecker.domain.validation.DomainValidation;

public record CheckedStudent(Integer id, Integer groupId, String fullName, String variantCode, CheckStatus checkStatus, BigDecimal totalScore) {

    public CheckedStudent {
        groupId = DomainValidation.requireNotNull(groupId, "Идентификатор группы не должен быть пустым");
        fullName = DomainValidation.requireText(fullName, "ФИО студента не должно быть пустым");
        variantCode = DomainValidation.requireText(variantCode, "Код варианта не должен быть пустым");
        checkStatus = DomainValidation.requireNotNull(checkStatus, "Статус проверки не должен быть пустым");
        if (totalScore == null) {
            totalScore = BigDecimal.ZERO;
        }
        totalScore = totalScore.stripTrailingZeros();
    }
}