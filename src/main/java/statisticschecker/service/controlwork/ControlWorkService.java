package statisticschecker.service.controlwork;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statisticschecker.domain.controlwork.ControlWork;
import statisticschecker.persistence.controlwork.*;
import statisticschecker.persistence.user.*;
import statisticschecker.service.common.DomainMapper;

@Service
public class ControlWorkService {
    private final ControlWorkRepository controlWorkRepository;
    private final AppUserRepository appUserRepository;
    private final DomainMapper domainMapper;

    public ControlWorkService(ControlWorkRepository controlWorkRepository, AppUserRepository appUserRepository, DomainMapper domainMapper) {
        this.controlWorkRepository = controlWorkRepository;
        this.appUserRepository = appUserRepository;
        this.domainMapper = domainMapper;
    }

    @Transactional
    public ControlWork createControlWork(Integer createdByUserId, String title, BigDecimal passingScore) {
        AppUserEntity user = findUser(createdByUserId);
        validateTitle(title);
        validatePassingScore(passingScore);

        ControlWorkEntity controlWork = new ControlWorkEntity(user, title.trim(), passingScore.stripTrailingZeros(), null, null);
        ControlWorkEntity savedControlWork = controlWorkRepository.save(controlWork);
        return domainMapper.toControlWork(savedControlWork);
    }

    @Transactional(readOnly = true)
    public ControlWork getControlWork(Integer controlWorkId) {
        return domainMapper.toControlWork(findControlWork(controlWorkId));
    }

    @Transactional(readOnly = true)
    public List<ControlWork> findControlWorksByUser(Integer createdByUserId) {
        findUser(createdByUserId);
        List<ControlWorkEntity> controlWorks = controlWorkRepository.findByCreatedByUserIdOrderByCreatedAtDesc(createdByUserId);
        List<ControlWork> result = new ArrayList<>();
        for (ControlWorkEntity controlWork : controlWorks) {
            result.add(domainMapper.toControlWork(controlWork));
        }
        return result;
    }

    @Transactional
    public ControlWork updatePassingScore(Integer controlWorkId, BigDecimal passingScore) {
        validatePassingScore(passingScore);
        ControlWorkEntity controlWork = findControlWork(controlWorkId);
        controlWork.updatePassingScore(passingScore.stripTrailingZeros());
        return domainMapper.toControlWork(controlWorkRepository.save(controlWork));
    }

    private AppUserEntity findUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Идентификатор пользователя не должен быть пустым");
        }
        Optional<AppUserEntity> user = appUserRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        return user.get();
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

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название контрольной работы не должно быть пустым");
        }
        if (title.trim().length() > 150) {
            throw new IllegalArgumentException("Название контрольной работы не должно быть длиннее 150 символов");
        }
    }

    private void validatePassingScore(BigDecimal passingScore) {
        if (passingScore == null) {
            throw new IllegalArgumentException("Проходной балл не должен быть пустым");
        }
        if (passingScore.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Проходной балл не должен быть отрицательным");
        }
    }
}