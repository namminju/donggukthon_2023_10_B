package igloo.repository;

import common.MemberType;
import igloo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);
    List<Member> findAllByType(MemberType type);
}
