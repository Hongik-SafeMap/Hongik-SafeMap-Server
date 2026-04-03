package Hongik_SafeMap_Server.domain.member.repository;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import Hongik_SafeMap_Server.vo.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailIgnoreCase(String email);
    List<Member> findAllByStatus(MemberStatus status);
    boolean existsByEmail(String email);
    long countByIsCredibleTrueAndStatus(MemberStatus status);
}
