package statisticschecker.persistence.entity;

import jakarta.persistence.*;

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

    protected VariantEntity() {
    }

    public VariantEntity(ControlWorkEntity controlWork, String code, String sourceFileName) {
        this.controlWork = controlWork;
        this.code = code;
        this.sourceFileName = sourceFileName;
    }

    public Integer getId() {
        return id;
    }

    public ControlWorkEntity getControlWork() {
        return controlWork;
    }

    public String getCode() {
        return code;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }
}