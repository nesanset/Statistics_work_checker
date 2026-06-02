package statisticschecker.web.dto.student;

import java.math.BigDecimal;
import statisticschecker.domain.result.CheckStatus;

public record StudentResponse(Integer id, Integer groupId, String fullName, String variantCode, CheckStatus checkStatus, BigDecimal totalScore) {
}
