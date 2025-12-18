package Hongik_SafeMap_Server.domain.member.emergency_contact.service;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.emergency_contact.repository.EmergencyContactRepository;
import Hongik_SafeMap_Server.domain.member.emergency_contact.dto.EmergencyContactRequest;
import Hongik_SafeMap_Server.domain.member.emergency_contact.dto.EmergencyContactResponse;
import Hongik_SafeMap_Server.domain.member.emergency_contact.domain.EmergencyContact;
import Hongik_SafeMap_Server.exception.MemberException;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static Hongik_SafeMap_Server.exception.ErrorMessage.EMERGENCY_CONTACT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmergencyContactService {
    private final EmergencyContactRepository emergencyContactRepository;
    private final MemberUtil memberUtil;

    public List<EmergencyContactResponse> getMyEmergencyContacts() {
        Member member = memberUtil.getLoggedInMember();

        return emergencyContactRepository.findAllByMemberId(member.getId()).stream()
                .map(EmergencyContactResponse::of)
                .toList();
    }

    @Transactional
    public Long createMyEmergencyContact(EmergencyContactRequest request) {
        Member member = memberUtil.getLoggedInMember();

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .member(member)
                .name(request.name())
                .relationship(request.relationship())
                .phone(request.phone())
                .build();

        return emergencyContactRepository.save(emergencyContact).getId();
    }

    @Transactional
    public EmergencyContactResponse updateMyEmergencyContact(Long emergencyContactId, EmergencyContactRequest request) {
        Member member = memberUtil.getLoggedInMember();

        EmergencyContact emergencyContact = emergencyContactRepository.findByIdAndMemberId(emergencyContactId, member.getId())
                .orElseThrow(() -> new MemberException(EMERGENCY_CONTACT_NOT_FOUND));
        emergencyContact.updateEmergencyContact(request.name(), request.relationship(), request.phone());

        return EmergencyContactResponse.of(emergencyContact);
    }

    @Transactional
    public void deleteMyEmergencyContact(Long emergencyContactId) {
        Member member = memberUtil.getLoggedInMember();

        boolean exists = emergencyContactRepository.existsByIdAndMemberId(emergencyContactId, member.getId());
        if (!exists) {
            throw new MemberException(EMERGENCY_CONTACT_NOT_FOUND);
        }

        emergencyContactRepository.deleteByIdAndMemberId(emergencyContactId, member.getId());
    }
}
