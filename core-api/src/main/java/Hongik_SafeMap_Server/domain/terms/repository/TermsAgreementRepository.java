package Hongik_SafeMap_Server.domain.terms.repository;

import Hongik_SafeMap_Server.domain.terms.domain.TermsAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TermsAgreementRepository extends JpaRepository<TermsAgreement, Long> {

    Optional<TermsAgreement> findByMemberIdAndTermsVersionId(Long memberId, Long termsVersionId);

    @Query("SELECT ta FROM TermsAgreement ta JOIN FETCH ta.termsVersion JOIN FETCH ta.privacyPolicyVersion WHERE ta.member.id = :memberId ORDER BY ta.createdAt DESC")
    List<TermsAgreement> findAllByMemberIdWithVersion(@Param("memberId") Long memberId);
}
