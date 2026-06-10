package statisticschecker.domain.importing;

import java.util.List;
import statisticschecker.domain.student.Student;
import statisticschecker.domain.validation.DomainValidation;
import statisticschecker.domain.variant.Variant;

public record ControlWorkImportData(String studentListFileName, String variantsRootPath, List<Student> students, List<Variant> variants) {

    public ControlWorkImportData {
        studentListFileName = DomainValidation.trimNullableText(studentListFileName);
        variantsRootPath = DomainValidation.trimNullableText(variantsRootPath);
        students = DomainValidation.requireNotEmptyList(students, "Для импорта должен быть передан хотя бы один студент");
        variants = DomainValidation.requireNotEmptyList(variants, "Для импорта должен быть передан хотя бы один вариант");
    }
}