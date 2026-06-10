package statisticschecker.persistence.assignment;

import jakarta.persistence.*;
import java.math.BigDecimal;
import statisticschecker.persistence.variant.VariantEntity;

@Entity
@Table(name = "assignments", uniqueConstraints = @UniqueConstraint(name = "uq_assignments_number", columnNames = {"variant_id", "number"}))
public class AssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private VariantEntity variant;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "max_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal maxScore;

    protected AssignmentEntity() {
    }

    public AssignmentEntity(int number, String text, BigDecimal maxScore) {
        this.number = number;
        this.text = text;
        this.maxScore = maxScore;
    }

    public Integer getId() {
        return id;
    }

    public VariantEntity getVariant() {
        return variant;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public void assignVariant(VariantEntity variant) {
        this.variant = variant;
    }
}