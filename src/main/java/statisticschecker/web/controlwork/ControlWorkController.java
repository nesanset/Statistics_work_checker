package statisticschecker.web.controlwork;

import java.math.BigDecimal;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import statisticschecker.domain.controlwork.ControlWork;
import statisticschecker.service.controlwork.*;
import statisticschecker.web.common.ResponseMapper;

@RestController
@RequestMapping("/api/control-works")
public class ControlWorkController {
    private final ControlWorkService controlWorkService;
    private final ControlWorkImportFacade controlWorkImportFacade;
    private final ResponseMapper responseMapper;

    public ControlWorkController(ControlWorkService controlWorkService, ControlWorkImportFacade controlWorkImportFacade, ResponseMapper responseMapper) {
        this.controlWorkService = controlWorkService;
        this.controlWorkImportFacade = controlWorkImportFacade;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    public ControlWorkResponse createControlWork(@RequestParam Integer createdByUserId, @RequestParam String title, @RequestParam BigDecimal passingScore) {
        ControlWork controlWork = controlWorkService.createControlWork(createdByUserId, title, passingScore);
        return responseMapper.toResponse(controlWork);
    }

    @GetMapping
    public List<ControlWorkResponse> findControlWorks(@RequestParam Integer createdByUserId) {
        List<ControlWork> controlWorks = controlWorkService.findControlWorksByUser(createdByUserId);
        return responseMapper.toControlWorkResponseList(controlWorks);
    }

    @GetMapping("/{controlWorkId}")
    public ControlWorkResponse getControlWork(@PathVariable Integer controlWorkId) {
        ControlWork controlWork = controlWorkService.getControlWork(controlWorkId);
        return responseMapper.toResponse(controlWork);
    }

    @PatchMapping("/{controlWorkId}/passing-score")
    public ControlWorkResponse updatePassingScore(@PathVariable Integer controlWorkId, @RequestParam BigDecimal passingScore) {
        ControlWork controlWork = controlWorkService.updatePassingScore(controlWorkId, passingScore);
        return responseMapper.toResponse(controlWork);
    }

    @PostMapping("/{controlWorkId}/import")
    public ResponseEntity<Void> importControlWork(@PathVariable Integer controlWorkId, @RequestParam String studentListFilePath, @RequestParam String variantsDirectoryPath) {
        Path studentListFile = buildPath(studentListFilePath, "Путь к файлу со студентами указан некорректно");
        Path variantsDirectory = buildPath(variantsDirectoryPath, "Путь к папке с вариантами указан некорректно");
        controlWorkImportFacade.importFromFiles(controlWorkId, studentListFile, variantsDirectory);
        return ResponseEntity.noContent().build();
    }

    private Path buildPath(String value, String errorMessage) {
        try {
            return Path.of(value);
        } catch (InvalidPathException exception) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}