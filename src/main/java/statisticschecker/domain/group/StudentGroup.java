package statisticschecker.domain.group;

import statisticschecker.domain.validation.DomainValidation;

public record StudentGroup(Integer id, String name) {

    public StudentGroup {
        name = DomainValidation.requireText(name, "Название группы не должно быть пустым");
    }
}