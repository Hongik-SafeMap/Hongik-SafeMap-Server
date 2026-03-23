package Hongik_SafeMap_Server.domain.member.service;

import Hongik_SafeMap_Server.domain.disaster_report.dto.response.DisasterReportListResponse;
import Hongik_SafeMap_Server.domain.disaster_report.service.DisasterReportService;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.domain.member.dto.request.MemberPasswordChangeRequest;
import Hongik_SafeMap_Server.domain.member.dto.response.MyPageResponse;
import Hongik_SafeMap_Server.domain.member.repository.MemberRepository;
import Hongik_SafeMap_Server.exception.MemberException;
import Hongik_SafeMap_Server.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Hongik_SafeMap_Server.exception.ErrorMessage.INVALID_CURRENT_PASSWORD;
import static Hongik_SafeMap_Server.exception.ErrorMessage.PASSWORD_SAME_AS_OLD;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;
    private final PasswordEncoder passwordEncoder;
    private final DisasterReportService disasterReportService;

    public MyPageResponse getMyPage() {
        Member member = memberUtil.getLoggedInMember();
        return MyPageResponse.of(member);
    }

    public Page<DisasterReportListResponse> getMyReports(int page, int size) {
        Member member = memberUtil.getLoggedInMember();
        return disasterReportService.getMyReports(page, size);
    }

    @Transactional
    public void updatePassword(MemberPasswordChangeRequest request) {
        Member member = memberUtil.getLoggedInMember();

        // 현재 비밀번호 일치 검증
        if (!passwordEncoder.matches(request.currentPassword(), member.getPassword())) {
            throw new MemberException(INVALID_CURRENT_PASSWORD);
        }

        // 새 비밀번호가 현재 비밀번호와 다른지 검증
        if (request.currentPassword().equals(request.newPassword())) {
            throw new MemberException(PASSWORD_SAME_AS_OLD);
        }

        String encodedPassword = passwordEncoder.encode(request.newPassword());
        member.updatePassword(encodedPassword);
    }
}
