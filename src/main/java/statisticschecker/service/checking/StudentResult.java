package statisticschecker.service.checking;

import java.math.BigDecimal;
import statisticschecker.domain.result.CheckStatus;

public record StudentResult(Integer id, Integer groupId, String fullName, String variantCode, CheckStatus checkStatus, BigDecimal totalScore) {
}
