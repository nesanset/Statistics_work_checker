package statisticschecker.web.controlwork;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ControlWorkResponse(Integer id, Integer createdByUserId, String title, BigDecimal passingScore, String studentListFileName, String variantsRootPath, LocalDateTime createdAt) {
}