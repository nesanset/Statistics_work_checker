package statisticschecker.service.checking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.report.GroupReport;
import statisticschecker.domain.student.*;
import statisticschecker.persistence.controlwork.ControlWorkEntity;
import statisticschecker.persistence.student.*;
import statisticschecker.persistence.group.*;
import statisticschecker.persistence.controlwork.ControlWorkRepository;
import statisticschecker.service.common.DomainMapper;

@Service
public class CheckingService {
    private final ControlWorkRepository controlWorkRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final StudentRepository studentRepository;
    private final StudentWorkService studentWorkService;
    private final DomainMapper domainMapper;

    public CheckingService(ControlWorkRepository controlWorkRepository, StudentGroupRepository studentGroupRepository, StudentRepository studentRepository, StudentWorkService studentWorkService, DomainMapper domainMapper) {
        this.controlWorkRepository = controlWorkRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.studentRepository = studentRepository;
        this.studentWorkService = studentWorkService;
        this.domainMapper = domainMapper;
    }

    @Transactional(readOnly = true)
    public List<StudentGroup> findGroupsByControlWork(Integer controlWorkId) {
        findControlWork(controlWorkId);
        List<StudentGroupEntity> groups = studentGroupRepository.findByControlWorkIdOrderByNameAsc(controlWorkId);

        List<StudentGroup> studentGroups = new ArrayList<>();
        for (StudentGroupEntity group : groups) {
            studentGroups.add(domainMapper.toStudentGroup(group));
        }
        return studentGroups;
    }

    @Transactional(readOnly = true)
    public List<CheckedStudent> findStudentsByGroup(Integer groupId) {
        findGroup(groupId);
        List<StudentEntity> students = studentRepository.findByStudentGroupIdOrderByFullNameAsc(groupId);

        List<CheckedStudent> checkedStudents = new ArrayList<>();
        for (StudentEntity student : students) {
            checkedStudents.add(studentWorkService.buildCheckedStudent(student));
        }
        return checkedStudents;
    }

    @Transactional(readOnly = true)
    public CheckedStudent findStudent(Integer studentId) {
        StudentEntity student = findStudentEntity(studentId);
        return studentWorkService.buildCheckedStudent(student);
    }

    @Transactional(readOnly = true)
    public List<CheckedAssignment> findAssignmentsByStudent(Integer studentId) {
        StudentEntity student = findStudentEntity(studentId);
        return studentWorkService.buildAssignments(student);
    }

    @Transactional(readOnly = true)
    public GroupReport buildGroupReport(Integer controlWorkId, Integer groupId) {
        ControlWorkEntity controlWork = findControlWork(controlWorkId);
        StudentGroupEntity group = findGroup(groupId);
        validateGroupBelongsToControlWork(controlWork, group);

        StudentGroup studentGroup = domainMapper.toStudentGroup(group);
        List<StudentEntity> students = studentRepository.findByStudentGroupIdOrderByFullNameAsc(groupId);
        List<StudentWork> studentWorks = new ArrayList<>();
        for (StudentEntity student : students) {
            studentWorks.add(studentWorkService.buildStudentWork(student, controlWork.getPassingScore()));
        }
        return new GroupReport(controlWork.getId(), controlWork.getTitle(), studentGroup, controlWork.getPassingScore(), studentWorks);
    }

    private ControlWorkEntity findControlWork(Integer controlWorkId) {
        if (controlWorkId == null) {
            throw new IllegalArgumentException("Идентификатор контрольной работы не должен быть пустым");
        }
        Optional<ControlWorkEntity> controlWork = controlWorkRepository.findById(controlWorkId);
        if (controlWork.isEmpty()) {
            throw new IllegalArgumentException("Контрольная работа не найдена");
        }
        return controlWork.get();
    }

    private StudentGroupEntity findGroup(Integer groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("Идентификатор группы не должен быть пустым");
        }
        Optional<StudentGroupEntity> group = studentGroupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new IllegalArgumentException("Группа не найдена");
        }
        return group.get();
    }

    private StudentEntity findStudentEntity(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Идентификатор студента не должен быть пустым");
        }
        Optional<StudentEntity> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new IllegalArgumentException("Студент не найден");
        }
        return student.get();
    }

    private void validateGroupBelongsToControlWork(ControlWorkEntity controlWork, StudentGroupEntity group) {
        if (!group.getControlWork().getId().equals(controlWork.getId())) {
            throw new IllegalArgumentException("Группа не относится к выбранной контрольной работе");
        }
    }
}