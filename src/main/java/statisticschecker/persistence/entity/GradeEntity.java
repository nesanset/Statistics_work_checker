package statisticschecker.persistence.entity;

import jakarta.persistence.*;
import statisticschecker.domain.grade.CommentTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades", uniqueConstraints = @UniqueConstraint(name = "uq_grades_student_assignment", columnNames = {"student_id", "assignment_id"}))
public class GradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private AssignmentEntity assignment;

    @Column(precision = 5, scale = 2)
    private BigDecimal score;

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_template", length = 40)
    private CommentTemplate commentTemplate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected GradeEntity() {
    }

    public GradeEntity(StudentEntity student, AssignmentEntity assignment, BigDecimal score, CommentTemplate commentTemplate) {
        this.student = student;
        this.assignment = assignment;
        this.score = score;
        this.commentTemplate = commentTemplate;
    }

    public Integer getId() {
        return id;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public AssignmentEntity getAssignment() {
        return assignment;
    }

    public BigDecimal getScore() {
        return score;
    }

    public CommentTemplate getCommentTemplate() {
        return commentTemplate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateScore(BigDecimal score, CommentTemplate commentTemplate) {
        this.score = score;
        this.commentTemplate = commentTemplate;
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    private void refreshUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }
}