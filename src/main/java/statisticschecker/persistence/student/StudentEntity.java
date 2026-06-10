package statisticschecker.persistence.student;

import jakarta.persistence.*;
import statisticschecker.domain.status.CheckStatus;
import statisticschecker.persistence.group.StudentGroupEntity;
import statisticschecker.persistence.variant.VariantEntity;

@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(name = "uq_students_group_full_name", columnNames = {"group_id", "full_name"}))
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private StudentGroupEntity studentGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private VariantEntity variant;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_status", nullable = false, length = 20)
    private CheckStatus checkStatus = CheckStatus.NOT_CHECKED;

    protected StudentEntity() {
    }

    public StudentEntity(VariantEntity variant, String fullName) {
        this.variant = variant;
        this.fullName = fullName;
    }

    public Integer getId() {
        return id;
    }

    public StudentGroupEntity getStudentGroup() {
        return studentGroup;
    }

    public VariantEntity getVariant() {
        return variant;
    }

    public String getFullName() {
        return fullName;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void updateCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public void assignGroup(StudentGroupEntity studentGroup) {
        this.studentGroup = studentGroup;
    }
}