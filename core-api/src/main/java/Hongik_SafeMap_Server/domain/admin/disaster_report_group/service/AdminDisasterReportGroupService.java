package Hongik_SafeMap_Server.domain.admin.disaster_report_group.service;

import Hongik_SafeMap_Server.domain.admin.disaster_report_group.dto.request.UpdateGroupTitleRequest;
import Hongik_SafeMap_Server.domain.disaster_report_group.domain.DisasterReportGroup;
import Hongik_SafeMap_Server.domain.disaster_report_group.repository.DisasterReportGroupRepository;
import Hongik_SafeMap_Server.exception.DisasterReportException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.DISASTER_REPORT_GROUP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminDisasterReportGroupService {

    private final DisasterReportGroupRepository disasterReportGroupRepository;

    @Transactional
    public void updateGroupTitle(Long groupId, UpdateGroupTitleRequest request) {
        DisasterReportGroup group = disasterReportGroupRepository.findById(groupId)
                .orElseThrow(() -> new DisasterReportException(DISASTER_REPORT_GROUP_NOT_FOUND));
        group.updateTitle(request.title());
    }
}
