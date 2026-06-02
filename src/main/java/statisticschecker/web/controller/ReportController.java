package statisticschecker.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import statisticschecker.domain.report.GroupReport;
import statisticschecker.service.ReportService;
import statisticschecker.web.dto.report.GroupReportResponse;
import statisticschecker.web.mapper.ReportResponseMapper;

@RestController
@RequestMapping("/api/control-works/{controlWorkId}/groups/{groupId}/report")
public class ReportController {
    private final ReportService reportService;
    private final ReportResponseMapper reportResponseMapper;

    public ReportController(ReportService reportService, ReportResponseMapper reportResponseMapper) {
        this.reportService = reportService;
        this.reportResponseMapper = reportResponseMapper;
    }

    @GetMapping
    public GroupReportResponse buildGroupReport(@PathVariable Integer controlWorkId, @PathVariable Integer groupId) {
        GroupReport report = reportService.buildGroupReport(controlWorkId, groupId);
        return reportResponseMapper.toResponse(report);
    }
}
