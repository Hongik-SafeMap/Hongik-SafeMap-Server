package Hongik_SafeMap_Server.domain.member.emergency_contact.controller;

import Hongik_SafeMap_Server.domain.member.emergency_contact.dto.EmergencyContactRequest;
import Hongik_SafeMap_Server.domain.member.emergency_contact.dto.EmergencyContactResponse;
import Hongik_SafeMap_Server.domain.member.emergency_contact.service.EmergencyContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/me/emergency-contacts")
public class EmergencyContactController {
    private final EmergencyContactService emergencyContactService;

    @GetMapping
    public ResponseEntity<List<EmergencyContactResponse>> getMyEmergencyContacts() {
        return ResponseEntity.ok(emergencyContactService.getMyEmergencyContacts());
    }

    @PostMapping
    public ResponseEntity<Long> createMyEmergencyContact(@Valid @RequestBody EmergencyContactRequest emergencyContactRequest) {
        Long id = emergencyContactService.createMyEmergencyContact(emergencyContactRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{emergencyContactId}")
    public ResponseEntity<EmergencyContactResponse> updateMyEmergencyContact(@PathVariable Long emergencyContactId, @Valid @RequestBody EmergencyContactRequest emergencyContactRequest) {
        return ResponseEntity.ok(emergencyContactService.updateMyEmergencyContact(emergencyContactId, emergencyContactRequest));
    }

    @DeleteMapping("/{emergencyContactId}")
    public ResponseEntity<Void> deleteMyEmergencyContact(@PathVariable Long emergencyContactId) {
        emergencyContactService.deleteMyEmergencyContact(emergencyContactId);
        return ResponseEntity.noContent().build();
    }
}
