package statisticschecker.persistence.grade;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import statisticschecker.domain.grade.CommentTemplate;
import statisticschecker.persistence.assignment.AssignmentEntity;
import statisticschecker.persistence.controlwork.ControlWorkEntity;
import statisticschecker.persistence.group.StudentGroupEntity;
import statisticschecker.persistence.student.StudentEntity;
import statisticschecker.persistence.user.AppUserEntity;
import statisticschecker.persistence.variant.VariantEntity;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class GradeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GradeRepository gradeRepository;

    @Test
    void findsGradeByStudentAndAssignment() {
        AppUserEntity user = entityManager.persist(new AppUserEntity("teacher", "hash"));
        ControlWorkEntity controlWork = entityManager.persist(new ControlWorkEntity(user, "Контрольная", new BigDecimal("10"), null, null));

        VariantEntity variant = new VariantEntity("1", "variant_1.xlsx");
        controlWork.addVariant(variant);
        entityManager.persist(variant);

        AssignmentEntity assignment = new AssignmentEntity(1, "Задание", new BigDecimal("2"));
        variant.addAssignment(assignment);
        entityManager.persist(assignment);

        StudentGroupEntity group = new StudentGroupEntity("Б23-901");
        controlWork.addGroup(group);
        entityManager.persist(group);

        StudentEntity student = new StudentEntity(variant, "Иванов Иван");
        group.addStudent(student);
        entityManager.persist(student);

        gradeRepository.save(new GradeEntity(student, assignment, new BigDecimal("1.5"), CommentTemplate.NO_COMMENT));

        Optional<GradeEntity> grade = gradeRepository.findByStudentIdAndAssignmentId(student.getId(), assignment.getId());

        assertThat(grade).isPresent();
        assertThat(grade.get().getScore()).isEqualByComparingTo("1.5");
    }
}
