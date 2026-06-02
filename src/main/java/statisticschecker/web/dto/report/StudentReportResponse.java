package statisticschecker.web.dto.report;

import java.math.BigDecimal;
import java.util.List;
import statisticschecker.domain.status.CheckStatus;
import statisticschecker.domain.status.PassingStatus;

public record StudentReportResponse(Integer studentId, String fullName, String variantCode, CheckStatus checkStatus, BigDecimal totalScore, PassingStatus passingStatus, List<AssignmentReportResponse> assignments) {
}
