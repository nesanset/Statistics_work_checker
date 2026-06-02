package statisticschecker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statisticschecker.domain.group.StudentGroup;
import statisticschecker.domain.report.DefaultGroupReportBuilder;
import statisticschecker.domain.report.GroupReport;
import statisticschecker.domain.report.GroupReportDirector;
import statisticschecker.domain.student.StudentWork;
import statisticschecker.persistence.entity.ControlWorkEntity;
import statisticschecker.persistence.entity.StudentEntity;
import statisticschecker.persistence.entity.StudentGroupEntity;
import statisticschecker.persistence.repository.ControlWorkRepository;
import statisticschecker.persistence.repository.StudentGroupRepository;
import statisticschecker.persistence.repository.StudentRepository;

@Service
public class ReportService {
    private final ControlWorkRepository controlWorkRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final StudentRepository studentRepository;
    private final StudentWorkAssembler studentWorkAssembler;

    public ReportService(ControlWorkRepository controlWorkRepository, StudentGroupRepository studentGroupRepository, StudentRepository studentRepository, StudentWorkAssembler studentWorkAssembler) {
        this.controlWorkRepository = controlWorkRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.studentRepository = studentRepository;
        this.studentWorkAssembler = studentWorkAssembler;
    }

    @Transactional(readOnly = true)
    public GroupReport buildGroupReport(Integer controlWorkId, Integer groupId) {
        ControlWorkEntity controlWork = findControlWork(controlWorkId);
        StudentGroupEntity group = findGroup(groupId);
        validateGroupBelongsToControlWork(controlWork, group);

        StudentGroup studentGroup = new StudentGroup(group.getId().longValue(), group.getName());
        List<StudentEntity> students = studentRepository.findByStudentGroupIdOrderByFullNameAsc(groupId);
        List<StudentWork> studentWorks = new ArrayList<>();
        for (StudentEntity student : students) {
            studentWorks.add(studentWorkAssembler.buildStudentWork(student, controlWork.getPassingScore()));
        }

        GroupReportDirector reportDirector = new GroupReportDirector(new DefaultGroupReportBuilder());
        return reportDirector.constructGroupReport(controlWork.getId().longValue(), controlWork.getTitle(), studentGroup, controlWork.getPassingScore(), studentWorks);
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

    private void validateGroupBelongsToControlWork(ControlWorkEntity controlWork, StudentGroupEntity group) {
        if (!group.getControlWork().getId().equals(controlWork.getId())) {
            throw new IllegalArgumentException("Группа не относится к выбранной контрольной работе");
        }
    }
}