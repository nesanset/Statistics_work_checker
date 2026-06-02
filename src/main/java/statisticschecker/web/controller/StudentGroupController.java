package statisticschecker.web.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.service.CheckingViewService;
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
        List<StudentGroup> groups = checkingViewService.findGroupsByControlWork(controlWorkId);
        return groupResponseMapper.toResponseList(groups);
    }
}
