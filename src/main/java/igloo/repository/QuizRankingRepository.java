package igloo.repository;

import igloo.domain.QuizRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRankingRepository extends JpaRepository<QuizRanking, Long> {
    Optional<QuizRanking> findById(Long id);

    // 특정 Igloo와 특정 Member의 랭킹을 찾는 메서드 추가
    Optional<QuizRanking> findByIglooIdAndMemberId(Long iglooId, Long memberId);
}
