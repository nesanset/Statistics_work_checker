package statisticschecker.service.controlwork;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statisticschecker.domain.assignment.Assignment;
import statisticschecker.domain.importing.ControlWorkImportData;
import statisticschecker.domain.student.Student;
import statisticschecker.domain.variant.Variant;
import statisticschecker.persistence.assignment.AssignmentEntity;
import statisticschecker.persistence.controlwork.*;
import statisticschecker.persistence.student.StudentEntity;
import statisticschecker.persistence.variant.*;
import statisticschecker.persistence.group.*;

@Service
public class ControlWorkImportService {
    private final ControlWorkRepository controlWorkRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final VariantRepository variantRepository;

    public ControlWorkImportService(ControlWorkRepository controlWorkRepository, StudentGroupRepository studentGroupRepository, VariantRepository variantRepository) {
        this.controlWorkRepository = controlWorkRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional
    public void importControlWorkData(Integer controlWorkId, ControlWorkImportData importData) {
        if (importData == null) {
            throw new IllegalArgumentException("Данные для импорта не должны быть пустыми");
        }

        ControlWorkEntity controlWork = findControlWork(controlWorkId);
        validateControlWorkState(controlWorkId);
        validateImportData(importData);

        controlWork.updateImportSources(importData.studentListFileName(), importData.variantsRootPath());

        Map<String, VariantEntity> variantsByCode = saveVariants(controlWork, importData);
        saveGroupsWithStudents(controlWork, importData, variantsByCode);
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

    private void validateControlWorkState(Integer controlWorkId) {
        if (studentGroupRepository.existsByControlWorkId(controlWorkId) || variantRepository.existsByControlWorkId(controlWorkId)) {
            throw new IllegalArgumentException("Для этой контрольной работы импорт уже был выполнен");
        }
    }

    private void validateImportData(ControlWorkImportData importData) {
        validateVariants(importData.variants());
        validateStudentVariants(importData);
    }

    private void validateVariants(List<Variant> variants) {
        Set<String> codes = new HashSet<>();
        for (Variant variant : variants) {
            if (!codes.add(variant.code())) {
                throw new IllegalArgumentException("Коды вариантов в импортируемых данных должны быть уникальными");
            }
            validateAssignments(variant.assignments(), variant.code());
        }
    }

    private void validateAssignments(List<Assignment> assignments, String variantCode) {
        Set<Integer> numbers = new HashSet<>();
        for (Assignment assignment : assignments) {
            if (!numbers.add(assignment.number())) {
                throw new IllegalArgumentException("Номера заданий в варианте " + variantCode + " должны быть уникальными");
            }
        }
    }

    private void validateStudentVariants(ControlWorkImportData importData) {
        Set<String> variantCodes = new HashSet<>();
        for (Variant variant : importData.variants()) {
            variantCodes.add(variant.code());
        }

        for (Student student : importData.students()) {
            if (!variantCodes.contains(student.variantCode())) {
                throw new IllegalArgumentException("Для студента " + student.fullName() + " указан несуществующий вариант " + student.variantCode());
            }
        }
    }

    private Map<String, VariantEntity> saveVariants(ControlWorkEntity controlWork, ControlWorkImportData importData) {
        Map<String, VariantEntity> variantsByCode = new HashMap<>();
        for (Variant variant : importData.variants()) {
            VariantEntity variantEntity = new VariantEntity(variant.code(), variant.sourceFileName());
            controlWork.addVariant(variantEntity);

            for (Assignment assignment : variant.assignments()) {
                AssignmentEntity assignmentEntity = new AssignmentEntity(assignment.number(), assignment.text(), assignment.maxScore());
                variantEntity.addAssignment(assignmentEntity);
            }
            VariantEntity savedVariant = variantRepository.save(variantEntity);
            variantsByCode.put(savedVariant.getCode(), savedVariant);
        }
        return variantsByCode;
    }

    private void saveGroupsWithStudents(ControlWorkEntity controlWork, ControlWorkImportData importData, Map<String, VariantEntity> variantsByCode) {
        Map<String, List<Student>> studentsByGroup = groupStudentsByName(importData.students());

        for (Map.Entry<String, List<Student>> entry : studentsByGroup.entrySet()) {
            StudentGroupEntity groupEntity = new StudentGroupEntity(entry.getKey());
            controlWork.addGroup(groupEntity);
            for (Student student : entry.getValue()) {
                VariantEntity variantEntity = variantsByCode.get(student.variantCode());
                StudentEntity studentEntity = new StudentEntity(variantEntity, student.fullName());
                groupEntity.addStudent(studentEntity);
            }
            studentGroupRepository.save(groupEntity);
        }
    }

    private Map<String, List<Student>> groupStudentsByName(List<Student> students) {
        Map<String, List<Student>> studentsByGroup = new LinkedHashMap<>();
        for (Student student : students) {
            if (!studentsByGroup.containsKey(student.groupName())) {
                studentsByGroup.put(student.groupName(), new ArrayList<>());
            }
            studentsByGroup.get(student.groupName()).add(student);
        }
        return studentsByGroup;
    }
}