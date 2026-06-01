package statisticschecker.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_groups", uniqueConstraints = @UniqueConstraint(name = "uq_student_groups_name", columnNames = {"control_work_id", "name"}))
public class StudentGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "control_work_id", nullable = false)
    private ControlWorkEntity controlWork;

    @Column(nullable = false, length = 30)
    private String name;

    protected StudentGroupEntity() {
    }

    public StudentGroupEntity(ControlWorkEntity controlWork, String name) {
        this.controlWork = controlWork;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public ControlWorkEntity getControlWork() {
        return controlWork;
    }

    public String getName() {
        return name;
    }
}