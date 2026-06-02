package statisticschecker.web.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import statisticschecker.service.checking.CheckingViewService;
import statisticschecker.service.checking.GroupResult;
import statisticschecker.web.dto.group.GroupResponse;
import statisticschecker.web.mapper.GroupResponseMapper;

@RestController
@RequestMapping("/api/control-works/{controlWorkId}/groups")
public class StudentGroupController {
    private final CheckingViewService checkingViewService;
    private final GroupResponseMapper groupResponseMapper;

    public StudentGroupController(CheckingViewService checkingViewService, GroupResponseMapper groupResponseMapper) {
        this.checkingViewService = checkingViewService;
        this.groupResponseMapper = groupResponseMapper;
    }

    @GetMapping
    public List<GroupResponse> findGroups(@PathVariable Integer controlWorkId) {
        List<GroupResult> results = checkingViewService.findGroupsByControlWork(controlWorkId);
        return groupResponseMapper.toResponseList(results);
    }
}