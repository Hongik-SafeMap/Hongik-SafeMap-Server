package Hongik_SafeMap_Server.domain.terms.repository;

import Hongik_SafeMap_Server.domain.terms.domain.TermsVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermsVersionRepository extends JpaRepository<TermsVersion, Long> {
    Optional<TermsVersion> findByVersion(String version);

    boolean existsByVersion(String version);

    boolean existsByVersionAndIdNot(String version, Long excludeId);

    Optional<TermsVersion> findFirstByOrderByCreatedAtDesc();
}
