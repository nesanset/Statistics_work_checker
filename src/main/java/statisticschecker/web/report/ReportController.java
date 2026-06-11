package statisticschecker.web.report;

import org.springframework.web.bind.annotation.*;
import statisticschecker.domain.report.GroupReport;
import statisticschecker.service.checking.CheckingService;
import statisticschecker.web.common.ResponseMapper;

@RestController
@RequestMapping("/api/control-works/{controlWorkId}/groups/{groupId}/report")
public class ReportController {
    private final CheckingService checkingService;
    private final ResponseMapper responseMapper;

    public ReportController(CheckingService checkingService, ResponseMapper responseMapper) {
        this.checkingService = checkingService;
        this.responseMapper = responseMapper;
    }

    @GetMapping
    public GroupReportResponse buildGroupReport(@PathVariable Integer controlWorkId, @PathVariable Integer groupId) {
        GroupReport report = checkingService.buildGroupReport(controlWorkId, groupId);
        return responseMapper.toResponse(report);
    }
}
