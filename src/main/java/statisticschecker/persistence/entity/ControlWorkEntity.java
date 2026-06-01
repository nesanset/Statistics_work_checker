package statisticschecker.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "control_works")
public class ControlWorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private AppUserEntity createdByUser;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "passing_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal passingScore;

    @Column(name = "student_list_file_name", length = 180)
    private String studentListFileName;

    @Column(name = "variants_root_path", length = 300)
    private String variantsRootPath;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected ControlWorkEntity() {
    }

    public ControlWorkEntity(AppUserEntity createdByUser, String title, BigDecimal passingScore, String studentListFileName, String variantsRootPath) {
        this.createdByUser = createdByUser;
        this.title = title;
        this.passingScore = passingScore;
        this.studentListFileName = studentListFileName;
        this.variantsRootPath = variantsRootPath;
    }

    public Integer getId() {
        return id;
    }

    public AppUserEntity getCreatedByUser() {
        return createdByUser;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPassingScore() {
        return passingScore;
    }

    public String getStudentListFileName() {
        return studentListFileName;
    }

    public String getVariantsRootPath() {
        return variantsRootPath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    private void fillCreatedAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}