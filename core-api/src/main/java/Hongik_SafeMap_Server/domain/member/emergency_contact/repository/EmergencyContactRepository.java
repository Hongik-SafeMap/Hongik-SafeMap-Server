package Hongik_SafeMap_Server.domain.member.emergency_contact.repository;

import Hongik_SafeMap_Server.domain.member.emergency_contact.domain.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
    List<EmergencyContact> findAllByMemberId(Long memberId);

    Optional<EmergencyContact> findByIdAndMemberId(Long id, Long memberId);

    void deleteByIdAndMemberId(Long id, Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);
}
