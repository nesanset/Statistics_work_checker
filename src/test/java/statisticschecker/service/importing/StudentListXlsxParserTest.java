package statisticschecker.service.importing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import statisticschecker.domain.student.Student;

class StudentListXlsxParserTest {

    @TempDir
    Path tempDir;

    @Test
    void readsStudentsFromFirstSheet() throws Exception {
        Path file = tempDir.resolve("students.xlsx");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Студенты");
            createRow(sheet, 0, "Группа", "ФИО студента", "Вариант");
            createRow(sheet, 1, "Б23-901", "Иванов Иван", "1");
            try (OutputStream outputStream = Files.newOutputStream(file)) {
                workbook.write(outputStream);
            }
        }

        List<Student> students = new StudentListXlsxParser().parse(file);

        assertThat(students).containsExactly(new Student("Б23-901", "Иванов Иван", "1"));
    }

    private void createRow(Sheet sheet, int rowIndex, String first, String second, String third) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(first);
        row.createCell(1).setCellValue(second);
        row.createCell(2).setCellValue(third);
    }
}
