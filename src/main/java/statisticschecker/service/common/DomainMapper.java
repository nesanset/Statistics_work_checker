package statisticschecker.service.common;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.controlwork.ControlWork;
import statisticschecker.domain.grade.*;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.CheckedStudent;
import statisticschecker.domain.user.AppUser;
import statisticschecker.persistence.assignment.AssignmentEntity;
import statisticschecker.persistence.controlwork.ControlWorkEntity;
import statisticschecker.persistence.grade.GradeEntity;
import statisticschecker.persistence.group.StudentGroupEntity;
import statisticschecker.persistence.student.StudentEntity;
import statisticschecker.persistence.user.AppUserEntity;

@Component
public class DomainMapper {

    public AppUser toAppUser(AppUserEntity user) {
        return new AppUser(user.getId(), user.getUsername(), user.getCreatedAt());
    }

    public ControlWork toControlWork(ControlWorkEntity controlWork) {
        return new ControlWork(controlWork.getId(), controlWork.getCreatedByUser().getId(), controlWork.getTitle(), controlWork.getPassingScore(), controlWork.getStudentListFileName(), controlWork.getVariantsRootPath(), controlWork.getCreatedAt());
    }

    public StudentGroup toStudentGroup(StudentGroupEntity group) {
        return new StudentGroup(group.getId(), group.getName());
    }

    public CheckedStudent toCheckedStudent(StudentEntity student, BigDecimal totalScore) {
        return new CheckedStudent(student.getId(), student.getStudentGroup().getId(), student.getFullName(), student.getVariant().getCode(), student.getCheckStatus(), totalScore);
    }

    public CheckedAssignment toCheckedAssignment(AssignmentEntity assignment, GradeEntity grade) {
        BigDecimal score = null;
        CommentTemplate commentTemplate = CommentTemplate.NO_COMMENT;
        if (grade != null) {
            score = grade.getScore();
            if (grade.getCommentTemplate() != null) {
                commentTemplate = grade.getCommentTemplate();
            }
        }
        return new CheckedAssignment(assignment.getId(), assignment.getVariant().getCode(), assignment.getNumber(), assignment.getText(), assignment.getMaxScore(), score, commentTemplate);
    }

    public GradeChange toGradeChange(StudentEntity student, AssignmentEntity assignment, GradeEntity grade) {
        return new GradeChange(student.getId(), assignment.getId(), grade.getScore(), grade.getCommentTemplate(), student.getCheckStatus());
    }
}