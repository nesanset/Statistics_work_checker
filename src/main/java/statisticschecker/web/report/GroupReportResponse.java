package statisticschecker.web.report;

import java.math.BigDecimal;
import java.util.List;

public record GroupReportResponse(Integer controlWorkId, String controlWorkTitle, Integer groupId, String groupName, BigDecimal passingScore, List<StudentReportResponse> students) {
}