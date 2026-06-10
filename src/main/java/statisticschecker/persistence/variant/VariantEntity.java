package statisticschecker.persistence.variant;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import statisticschecker.persistence.assignment.AssignmentEntity;
import statisticschecker.persistence.controlwork.ControlWorkEntity;

@Entity
@Table(name = "variants", uniqueConstraints = @UniqueConstraint(name = "uq_variants_code", columnNames = {"control_work_id", "code"}))
public class VariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "control_work_id", nullable = false)
    private ControlWorkEntity controlWork;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(name = "source_file_name", length = 180)
    private String sourceFileName;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentEntity> assignments = new ArrayList<>();

    protected VariantEntity() {
    }

    public VariantEntity(String code, String sourceFileName) {
        this.code = code;
        this.sourceFileName = sourceFileName;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public List<AssignmentEntity> getAssignments() {
        return assignments;
    }

    public void addAssignment(AssignmentEntity assignment) {
        assignment.assignVariant(this);
        assignments.add(assignment);
    }

    public void assignControlWork(ControlWorkEntity controlWork) {
        this.controlWork = controlWork;
    }
}