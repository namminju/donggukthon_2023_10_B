package igloo.service;

import igloo.config.security.UserAuthorize;
import igloo.domain.Igloo;
import igloo.domain.Member;
import igloo.domain.QuizRanking;
import igloo.dto.quizRanking.QuizRankingResponse;
import igloo.repository.IglooRepository;
import igloo.repository.QuizRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@UserAuthorize
@RequiredArgsConstructor
@Service
public class QuizRankingService {
    private final IglooRepository iglooRepository;
    private final MemberService memberService;
    private final QuizRankingRepository quizRankingRepository;

    @Transactional
    public void updateQuizRanking(String token, Long iglooId, Long score) {

        Member member = memberService.getUserByToken(token);
        Igloo igloo = getValidatedIgloo(iglooId);
        boolean isWriter = igloo.getOwner().getId().equals(member.getId());

        // 소유자 여부 판단
        if (isWriter) {
            // 소유자가 아닌 경우 예외 처리
            throw new IllegalArgumentException("You are the writer of this Igloo");
        }
        System.out.println("do it");
        igloo.addOrUpdateRanking(member, score);
    }

    @Transactional(readOnly = true)
    public List<QuizRankingResponse> getQuizRankings(Long iglooId) {
        // Igloo 찾기
        Igloo igloo = getValidatedIgloo(iglooId);

        // 방문자 리스트 반환
        return QuizRankingResponse.of(igloo.getRankingsSortedByScore());
    }

    @Transactional(readOnly = true)
    public QuizRankingResponse getMemberQuizRanking(String token, Long iglooId) {
        Member member = memberService.getUserByToken(token);

        // 특정 Igloo & Member 대한 퀴즈 랭킹 조회
        return quizRankingRepository.findByIglooIdAndMemberId(iglooId, member.getId())
                .map(QuizRankingResponse::of) // 존재하면 변환
                .orElseThrow(() -> new IllegalArgumentException("해당 Igloo 랭킹 기록이 없습니다."));
    }

    private Igloo getValidatedIgloo(Long id) {
        // Igloo 찾기 및 예외 처리
        return getIglooByMemberId(id)
                .orElseThrow(() -> new IllegalArgumentException("Igloo not found for this member"));
    }

    public Optional<Igloo> getIglooByMemberId(Long memberId) {
        return iglooRepository.findByOwnerId(memberId); // Member id로 Igloo 찾기
    }
}
