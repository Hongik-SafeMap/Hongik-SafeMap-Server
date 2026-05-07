package Hongik_SafeMap_Server.domain.privacy_policy.repository;

import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicy;
import Hongik_SafeMap_Server.domain.privacy_policy.domain.PrivacyPolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrivacyPolicyRepository extends JpaRepository<PrivacyPolicy, Long> {

    @Query("SELECT p FROM PrivacyPolicy p JOIN FETCH p.privacyPolicyVersion")
    List<PrivacyPolicy> findAllWithVersion();

    @Query("SELECT p FROM PrivacyPolicy p JOIN FETCH p.privacyPolicyVersion WHERE p.id IN :ids")
    List<PrivacyPolicy> findAllByIdWithVersion(@Param("ids") List<Long> ids);

    @Query("SELECT p FROM PrivacyPolicy p JOIN FETCH p.privacyPolicyVersion pv WHERE pv = :privacyPolicyVersion")
    List<PrivacyPolicy> findAllByPrivacyPolicyVersion(@Param("privacyPolicyVersion") PrivacyPolicyVersion privacyPolicyVersion);
}
