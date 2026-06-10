package statisticschecker.persistence.group;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import statisticschecker.persistence.controlwork.ControlWorkEntity;
import statisticschecker.persistence.student.StudentEntity;

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

    @OneToMany(mappedBy = "studentGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentEntity> students = new ArrayList<>();

    protected StudentGroupEntity() {
    }

    public StudentGroupEntity(String name) {
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

    public List<StudentEntity> getStudents() {
        return students;
    }

    public void addStudent(StudentEntity student) {
        student.assignGroup(this);
        students.add(student);
    }

    public void assignControlWork(ControlWorkEntity controlWork) {
        this.controlWork = controlWork;
    }
}