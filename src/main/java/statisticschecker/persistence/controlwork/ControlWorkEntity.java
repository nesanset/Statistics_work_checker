package statisticschecker.persistence.controlwork;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import statisticschecker.persistence.group.StudentGroupEntity;
import statisticschecker.persistence.user.AppUserEntity;
import statisticschecker.persistence.variant.VariantEntity;

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

    @OneToMany(mappedBy = "controlWork", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentGroupEntity> groups = new ArrayList<>();

    @OneToMany(mappedBy = "controlWork", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariantEntity> variants = new ArrayList<>();

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

    public List<StudentGroupEntity> getGroups() {
        return groups;
    }

    public List<VariantEntity> getVariants() {
        return variants;
    }

    public void addGroup(StudentGroupEntity group) {
        group.assignControlWork(this);
        groups.add(group);
    }

    public void addVariant(VariantEntity variant) {
        variant.assignControlWork(this);
        variants.add(variant);
    }

    public void updateImportSources(String studentListFileName, String variantsRootPath) {
        this.studentListFileName = studentListFileName;
        this.variantsRootPath = variantsRootPath;
    }

    public void updatePassingScore(BigDecimal passingScore) {
        this.passingScore = passingScore;
    }
}