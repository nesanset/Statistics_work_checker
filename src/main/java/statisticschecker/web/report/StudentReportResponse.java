package statisticschecker.web.report;

import java.math.BigDecimal;
import java.util.List;
import statisticschecker.domain.status.*;
import statisticschecker.web.checking.CheckedAssignmentResponse;

public record StudentReportResponse(Integer studentId, String fullName, String variantCode, CheckStatus checkStatus, BigDecimal totalScore, PassingStatus passingStatus, List<CheckedAssignmentResponse> assignments) {
}