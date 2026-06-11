package statisticschecker.service.importing;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.poi.ss.usermodel.*;

class ExcelReader {
    private final DataFormatter dataFormatter = new DataFormatter();

    void validateXlsxFile(Path filePath, String fileDescription) {
        if (filePath == null) {
            throw new IllegalArgumentException(fileDescription + " не должен быть пустым");
        }
        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            throw new IllegalArgumentException(fileDescription + " не найден");
        }
        if (!filePath.getFileName().toString().toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException(fileDescription + " должен иметь формат xlsx");
        }
    }

    void validateHeader(Sheet sheet, String fileDescription, String firstHeader, String secondHeader, String thirdHeader) {
        Row header = sheet.getRow(0);
        if (header == null) {
            throw new IllegalArgumentException(fileDescription + " не содержит строку заголовков");
        }
        if (!readText(header.getCell(0)).equals(firstHeader) || !readText(header.getCell(1)).equals(secondHeader) || !readText(header.getCell(2)).equals(thirdHeader)) {
            throw new IllegalArgumentException(fileDescription + " должен содержать заголовки: " + firstHeader + ", " + secondHeader + ", " + thirdHeader);
        }
    }

    String readText(Cell cell) {
        if (cell == null) {
            return "";
        }
        String value = dataFormatter.formatCellValue(cell);
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    BigDecimal readDecimal(Cell cell, int rowNumber) {
        String value = readText(cell);
        if (value.isBlank()) {
            throw new IllegalArgumentException("В строке " + rowNumber + " не указан максимальный балл");
        }
        value = value.replace(',', '.');
        try {
            return new BigDecimal(value).stripTrailingZeros();
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("В строке " + rowNumber + " максимальный балл должен быть числом");
        }
    }

    int readInteger(Cell cell, int rowNumber) {
        String value = readText(cell);
        if (value.isBlank()) {
            throw new IllegalArgumentException("В строке " + rowNumber + " не указан номер задания");
        }
        try {
            BigDecimal decimal = new BigDecimal(value.replace(',', '.'));
            return decimal.intValueExact();
        } catch (ArithmeticException | NumberFormatException exception) {
            throw new IllegalArgumentException("В строке " + rowNumber + " номер задания должен быть целым числом");
        }
    }
}