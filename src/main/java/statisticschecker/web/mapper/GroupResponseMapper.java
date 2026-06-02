package statisticschecker.web.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.web.dto.group.GroupResponse;

@Component
public class GroupResponseMapper {

    public GroupResponse toResponse(StudentGroup group) {
        return new GroupResponse(group.id().intValue(), group.name());
    }

    public List<GroupResponse> toResponseList(List<StudentGroup> groups) {
        List<GroupResponse> responses = new ArrayList<>();
        for (StudentGroup group : groups) {
            responses.add(toResponse(group));
        }
        return responses;
    }
}
