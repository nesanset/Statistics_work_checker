package statisticschecker.service.importing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import statisticschecker.domain.student.Student;

@Component
public class StudentListXlsxParser {
    private final ExcelReader excelReader = new ExcelReader();

    public List<Student> parse(Path filePath) {
        excelReader.validateXlsxFile(filePath, "Файл со списком студентов");
        try (InputStream inputStream = Files.newInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalArgumentException("Файл со списком студентов не содержит листов");
            }
            return parseSheet(workbook.getSheetAt(0));
        } catch (IOException exception) {
            throw new IllegalArgumentException("Не удалось прочитать файл со списком студентов", exception);
        }
    }

    private List<Student> parseSheet(Sheet sheet) {
        excelReader.validateHeader(sheet, "Файл со списком студентов", "Группа", "ФИО студента", "Вариант");
        List<Student> students = new ArrayList<>();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null || excelReader.readText(row.getCell(0)).isBlank()) {
                continue;
            }
            String groupName = excelReader.readText(row.getCell(0));
            String fullName = excelReader.readText(row.getCell(1));
            String variantCode = excelReader.readText(row.getCell(2));
            validateStudentRow(groupName, fullName, variantCode, rowIndex + 1);

            students.add(new Student(groupName, fullName, variantCode));
        }
        if (students.isEmpty()) {
            throw new IllegalArgumentException("В файле со списком студентов не найдено ни одной строки со студентом");
        }
        return students;
    }

    private void validateStudentRow(String groupName, String fullName, String variantCode, int rowNumber) {
        if (groupName.isBlank()) {
            throw new IllegalArgumentException("В строке " + rowNumber + " не указана группа");
        }
        if (fullName.isBlank()) {
            throw new IllegalArgumentException("В строке " + rowNumber + " не указано ФИО студента");
        }
        if (variantCode.isBlank()) {
            throw new IllegalArgumentException("В строке " + rowNumber + " не указан вариант студента");
        }
    }
}