package igloo.repository;

import igloo.domain.Igloo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IglooRepository extends JpaRepository<Igloo, Long> {
    Optional<Igloo> findByOwnerId(Long id);  // Member의 id를 기준으로 Igloo 찾기
    List<Igloo> findByInvitationCodeContaining(String invitationCode); // invitationCode가 포함된 Igloo 리스트 반환
    boolean existsByInvitationCode(String invitationCode);//중복여부 확인
}
