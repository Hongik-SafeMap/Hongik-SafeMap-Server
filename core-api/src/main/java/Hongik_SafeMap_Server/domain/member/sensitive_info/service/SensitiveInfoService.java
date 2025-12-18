package Hongik_SafeMap_Server.domain.member.sensitive_info.service;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.sensitive_info.domain.SensitiveInfo;
import Hongik_SafeMap_Server.domain.member.sensitive_info.repository.SensitiveInfoRepository;
import Hongik_SafeMap_Server.domain.member.sensitive_info.dto.SensitiveInfoRequest;
import Hongik_SafeMap_Server.domain.member.sensitive_info.dto.SensitiveInfoResponse;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SensitiveInfoService {
    private final SensitiveInfoRepository sensitiveInfoRepository;
    private final MemberUtil memberUtil;

    // 내 민감정보 조회(없으면 null 반환)
    public SensitiveInfoResponse getMySensitiveInfo() {
        Member member = memberUtil.getLoggedInMember();

        return sensitiveInfoRepository.findByMemberId(member.getId())
                .map(SensitiveInfoResponse::of)
                .orElse(null);
    }

    // 내 민감정보 생성 또는 수정
    @Transactional
    public SensitiveInfoResponse upsertMySensitiveInfo(SensitiveInfoRequest request) {
        Member member = memberUtil.getLoggedInMember();

        SensitiveInfo sensitiveInfo = sensitiveInfoRepository.findByMemberId(member.getId())
                .orElseGet(() -> SensitiveInfo.builder()
                        .member(member)
                        .build());

        sensitiveInfo.updateSensitiveInfo(
                request.bloodType(),
                request.allergies(),
                request.chronicDiseases(),
                request.medications()
        );

        return SensitiveInfoResponse.of(sensitiveInfoRepository.save(sensitiveInfo));
    }
}
