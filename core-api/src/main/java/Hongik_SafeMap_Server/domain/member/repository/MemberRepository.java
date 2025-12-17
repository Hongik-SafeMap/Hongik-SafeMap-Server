package Hongik_SafeMap_Server.domain.member.repository;

import Hongik_SafeMap_Server.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);
}
