package statisticschecker.web.dto.grade;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import statisticschecker.domain.grade.CommentTemplate;

public record UpdateGradeRequest(@NotNull(message = "Балл не должен быть пустым") @PositiveOrZero(message = "Балл не должен быть отрицательным") BigDecimal score, CommentTemplate commentTemplate) {
}