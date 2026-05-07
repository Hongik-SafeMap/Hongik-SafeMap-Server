package Hongik_SafeMap_Server.domain.privacy_policy.repository;

import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivacyPolicyVersionRepository extends JpaRepository<PrivacyPolicyVersion, Long> {
    Optional<PrivacyPolicyVersion> findByVersion(String version);

    boolean existsByVersionAndIdNot(String version, Long excludeId);

    Optional<PrivacyPolicyVersion> findFirstByOrderByCreatedAtDesc();
}
