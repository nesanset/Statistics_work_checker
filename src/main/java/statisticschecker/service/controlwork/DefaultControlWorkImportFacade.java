package statisticschecker.service.controlwork;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.springframework.stereotype.Service;
import statisticschecker.domain.importing.ControlWorkImportData;
import statisticschecker.domain.student.Student;
import statisticschecker.domain.variant.Variant;
import statisticschecker.service.importing.*;

@Service
public class DefaultControlWorkImportFacade implements ControlWorkImportFacade {
    private final StudentListXlsxParser studentListXlsxParser;
    private final VariantXlsxParser variantXlsxParser;
    private final ControlWorkImportService controlWorkImportService;

    public DefaultControlWorkImportFacade(StudentListXlsxParser studentListXlsxParser, VariantXlsxParser variantXlsxParser, ControlWorkImportService controlWorkImportService) {
        this.studentListXlsxParser = studentListXlsxParser;
        this.variantXlsxParser = variantXlsxParser;
        this.controlWorkImportService = controlWorkImportService;
    }

    @Override
    public void importFromFiles(Integer controlWorkId, Path studentListFile, Path variantsDirectory) {
        validateVariantsDirectory(variantsDirectory);
        List<Student> students = studentListXlsxParser.parse(studentListFile);
        List<Variant> variants = loadVariants(variantsDirectory);
        ControlWorkImportData importData = new ControlWorkImportData(studentListFile.getFileName().toString(), variantsDirectory.toAbsolutePath().toString(), students, variants);
        controlWorkImportService.importControlWorkData(controlWorkId, importData);
    }

    private List<Variant> loadVariants(Path variantsDirectory) {
        List<Path> variantFiles = findVariantFiles(variantsDirectory);
        List<Variant> variants = new ArrayList<>();
        for (Path variantFile : variantFiles) {
            variants.add(variantXlsxParser.parse(variantFile));
        }
        return variants;
    }

    private List<Path> findVariantFiles(Path variantsDirectory) {
        List<Path> variantFiles = new ArrayList<>();
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(variantsDirectory)) {
            for (Path path : paths) {
                if (Files.isRegularFile(path) && path.getFileName().toString().toLowerCase().endsWith(".xlsx")) {
                    variantFiles.add(path);
                }
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException("Не удалось прочитать папку с файлами вариантов", exception);
        }

        Collections.sort(variantFiles);
        if (variantFiles.isEmpty()) {
            throw new IllegalArgumentException("В папке вариантов не найдено ни одного xlsx-файла");
        }
        return variantFiles;
    }

    private void validateVariantsDirectory(Path variantsDirectory) {
        if (variantsDirectory == null) {
            throw new IllegalArgumentException("Папка с вариантами не должна быть пустой");
        }
        if (!Files.exists(variantsDirectory) || !Files.isDirectory(variantsDirectory)) {
            throw new IllegalArgumentException("Папка с вариантами не найдена");
        }
    }
}