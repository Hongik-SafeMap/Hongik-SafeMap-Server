package Hongik_SafeMap_Server.domain.member.sensitive_info.repository;

import Hongik_SafeMap_Server.domain.member.sensitive_info.domain.SensitiveInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensitiveInfoRepository extends JpaRepository<SensitiveInfo, Long> {
    Optional<SensitiveInfo> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
