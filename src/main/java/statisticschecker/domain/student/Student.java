package statisticschecker.domain.student;

import statisticschecker.domain.validation.DomainValidation;

public record Student(String groupName, String fullName, String variantCode) {

    public Student {
        groupName = DomainValidation.requireText(groupName, "Название группы не должно быть пустым");
        fullName = DomainValidation.requireText(fullName, "ФИО студента не должно быть пустым");
        variantCode = DomainValidation.requireText(variantCode, "Код варианта не должен быть пустым");
    }
}