package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.service.checking.GroupResult;
import statisticschecker.web.dto.group.GroupResponse;

@Component
public class GroupResponseMapper {

    public GroupResponse toResponse(GroupResult result) {
        return new GroupResponse(result.id(), result.name());
    }

    public List<GroupResponse> toResponseList(List<GroupResult> results) {
        List<GroupResponse> responses = new ArrayList<>();
        for (GroupResult result : results) {
            responses.add(toResponse(result));
        }
        return responses;
    }
}
