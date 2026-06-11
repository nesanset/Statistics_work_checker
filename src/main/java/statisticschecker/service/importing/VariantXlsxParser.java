package statisticschecker.service.importing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import statisticschecker.domain.assignment.Assignment;
import statisticschecker.domain.variant.Variant;

@Component
public class VariantXlsxParser {
    private final ExcelReader excelReader = new ExcelReader();

    public Variant parse(Path filePath) {
        excelReader.validateXlsxFile(filePath, "Файл варианта");
        try (InputStream inputStream = Files.newInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalArgumentException("Файл варианта " + filePath.getFileName() + " не содержит листов");
            }
            Sheet lastSheet = workbook.getSheetAt(workbook.getNumberOfSheets() - 1);
            String variantCode = resolveVariantCode(filePath);
            List<Assignment> assignments = parseAssignments(lastSheet, filePath, variantCode);
            return new Variant(variantCode, filePath.getFileName().toString(), assignments);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Не удалось прочитать файл варианта " + filePath.getFileName(), exception);
        }
    }

    private List<Assignment> parseAssignments(Sheet sheet, Path filePath, String variantCode) {
        excelReader.validateHeader(sheet, "Последний лист файла " + filePath.getFileName(), "№ задания", "Текст задания", "Максимальный балл");
        List<Assignment> assignments = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null || excelReader.readText(row.getCell(0)).isBlank()) {
                continue;
            }
            int rowNumber = rowIndex + 1;
            int number = excelReader.readInteger(row.getCell(0), rowNumber);
            String text = excelReader.readText(row.getCell(1));
            if (text.isBlank()) {
                throw new IllegalArgumentException("В строке " + rowNumber + " не указан текст задания");
            }
            assignments.add(new Assignment(variantCode, number, text, excelReader.readDecimal(row.getCell(2), rowNumber)));
        }
        if (assignments.isEmpty()) {
            throw new IllegalArgumentException("На последнем листе файла " + filePath.getFileName() + " не найдено ни одного задания");
        }
        return assignments;
    }

    private String resolveVariantCode(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileName = fileName.substring(0, dotIndex);
        }
        String normalized = fileName.trim();
        normalized = normalized.replaceFirst("(?i)^variant[_\\s-]*", "");
        normalized = normalized.replaceFirst("(?i)^вариант[_\\s-]*", "");
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Не удалось определить код варианта из имени файла " + filePath.getFileName());
        }
        return normalized;
    }
}