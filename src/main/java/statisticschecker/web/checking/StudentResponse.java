package statisticschecker.web.checking;

import java.math.BigDecimal;
import statisticschecker.domain.status.CheckStatus;

public record StudentResponse(Integer id, Integer groupId, String fullName, String variantCode, CheckStatus checkStatus, BigDecimal totalScore) {
}
