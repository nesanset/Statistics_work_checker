package statisticschecker.service.importing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import statisticschecker.domain.assignment.Assignment;
import statisticschecker.domain.variant.Variant;

class VariantXlsxParserTest {

    @TempDir
    Path tempDir;

    @Test
    void readsAssignmentsFromLastSheet() throws Exception {
        Path file = tempDir.resolve("variant_1.xlsx");
        try (Workbook workbook = new XSSFWorkbook()) {
            workbook.createSheet("Черновик");
            Sheet sheet = workbook.createSheet("Задания");
            createRow(sheet, 0, "№ задания", "Текст задания", "Максимальный балл");
            createRow(sheet, 1, "1", "Найти среднее", "2.5");
            try (OutputStream outputStream = Files.newOutputStream(file)) {
                workbook.write(outputStream);
            }
        }

        Variant variant = new VariantXlsxParser().parse(file);

        assertThat(variant.code()).isEqualTo("1");
        assertThat(variant.assignments()).containsExactly(new Assignment("1", 1, "Найти среднее", new BigDecimal("2.5")));
    }

    private void createRow(Sheet sheet, int rowIndex, String first, String second, String third) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(first);
        row.createCell(1).setCellValue(second);
        row.createCell(2).setCellValue(third);
    }
}
