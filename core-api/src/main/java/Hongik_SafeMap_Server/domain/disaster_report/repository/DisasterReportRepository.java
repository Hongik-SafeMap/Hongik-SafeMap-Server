package Hongik_SafeMap_Server.domain.disaster_report.repository;

import Hongik_SafeMap_Server.domain.disaster_report.domain.DisasterReport;
import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.DisasterReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisasterReportRepository extends JpaRepository<DisasterReport, Long> {

    // 긴급 제보 전체 조회
    Page<DisasterReport> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 마이페이지 - 내가 작성한 제보 목록
    Page<DisasterReport> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    // 전체 제보 수
    long count();

    // 관리자 제보 검토 - 최신 제보 N개 가져오기
    List<DisasterReport> findTop10ByOrderByCreatedAtDesc();

    // 관리자 제보 검토 - 상태별 조회(승인/블라인드/허위)
    Page<DisasterReport> findByStatusOrderByCreatedAtDesc(DisasterReportStatus status, Pageable pageable);
}
