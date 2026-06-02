package statisticschecker.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statisticschecker.domain.assignment.CheckedAssignment;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.student.CheckedStudent;
import statisticschecker.persistence.entity.*;
import statisticschecker.persistence.repository.*;

@Service
public class CheckingViewService {
    private final ControlWorkRepository controlWorkRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final StudentRepository studentRepository;
    private final StudentWorkAssembler studentWorkAssembler;

    public CheckingViewService(ControlWorkRepository controlWorkRepository, StudentGroupRepository studentGroupRepository, StudentRepository studentRepository, StudentWorkAssembler studentWorkAssembler) {
        this.controlWorkRepository = controlWorkRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.studentRepository = studentRepository;
        this.studentWorkAssembler = studentWorkAssembler;
    }

    @Transactional(readOnly = true)
    public List<StudentGroup> findGroupsByControlWork(Integer controlWorkId) {
        findControlWork(controlWorkId);
        List<StudentGroupEntity> groups = studentGroupRepository.findByControlWorkIdOrderByNameAsc(controlWorkId);

        List<StudentGroup> studentGroups = new ArrayList<>();
        for (StudentGroupEntity group : groups) {
            studentGroups.add(new StudentGroup(group.getId().longValue(), group.getName()));
        }
        return studentGroups;
    }

    @Transactional(readOnly = true)
    public List<CheckedStudent> findStudentsByGroup(Integer groupId) {
        findGroup(groupId);
        List<StudentEntity> students = studentRepository.findByStudentGroupIdOrderByFullNameAsc(groupId);

        List<CheckedStudent> checkedStudents = new ArrayList<>();
        for (StudentEntity student : students) {
            checkedStudents.add(studentWorkAssembler.buildCheckedStudent(student));
        }
        return checkedStudents;
    }

    @Transactional(readOnly = true)
    public CheckedStudent findStudent(Integer studentId) {
        StudentEntity student = findStudentEntity(studentId);
        return studentWorkAssembler.buildCheckedStudent(student);
    }

    @Transactional(readOnly = true)
    public List<CheckedAssignment> findAssignmentsByStudent(Integer studentId) {
        StudentEntity student = findStudentEntity(studentId);
        return studentWorkAssembler.buildCheckedAssignments(student);
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
}