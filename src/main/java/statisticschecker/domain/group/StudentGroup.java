package statisticschecker.domain.group;

public record StudentGroup(Long id, String name) {

    public StudentGroup {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название группы не должно быть пустым");
        }
        name = name.trim();
    }
}