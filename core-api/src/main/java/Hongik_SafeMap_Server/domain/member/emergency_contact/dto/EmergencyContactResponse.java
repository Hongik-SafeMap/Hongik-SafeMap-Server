package Hongik_SafeMap_Server.domain.member.emergency_contact.dto;

import Hongik_SafeMap_Server.domain.member.emergency_contact.domain.EmergencyContact;

public record EmergencyContactResponse(
        Long emergencyContactId,
        String name,
        String relationship,
        String phone
) {
    public static EmergencyContactResponse of(EmergencyContact emergencyContact) {
        return new EmergencyContactResponse(
                emergencyContact.getId(),
                emergencyContact.getName(),
                emergencyContact.getRelationship(),
                emergencyContact.getPhone()
        );
    }
}
