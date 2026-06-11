package statisticschecker.service.controlwork;

import java.nio.file.Path;

public interface ControlWorkImportFacade {
    void importFromFiles(Integer controlWorkId, Path studentListFile, Path variantsDirectory);
}