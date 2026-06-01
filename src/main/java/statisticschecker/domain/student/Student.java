package statisticschecker.domain.student;

public record Student(Long id, Long groupId, String fullName, String variantCode) {

    public Student {
        if (groupId == null) {
            throw new IllegalArgumentException("Идентификатор группы не должен быть пустым");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("ФИО студента не должно быть пустым");
        }
        if (variantCode == null || variantCode.isBlank()) {
            throw new IllegalArgumentException("Код варианта не должен быть пустым");
        }
        fullName = fullName.trim();
        variantCode = variantCode.trim();
    }
}