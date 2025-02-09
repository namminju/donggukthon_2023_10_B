package igloo.service;

import igloo.config.security.UserAuthorize;
import igloo.domain.Igloo;
import igloo.domain.Member;
import igloo.domain.Quiz;
import igloo.dto.quiz.QuizDetailInfoResponse;
import igloo.dto.quiz.QuizInfoResponse;
import igloo.dto.quiz.QuizUpdateResponse;
import igloo.repository.IglooRepository;
import igloo.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@UserAuthorize
@RequiredArgsConstructor
@Service
public class QuizService {
    private final IglooRepository iglooRepository;
    private final MemberService memberService;
    private final QuizRepository quizRepository;

    public Optional<Igloo> getIglooByMemberId(Long memberId) {
        return iglooRepository.findByOwnerId(memberId); // Member의 id로 Igloo 찾기
    }

    @Transactional(readOnly = true)
    public List<QuizInfoResponse> getQuizzes(Long id) {
        // Igloo 찾기
        Igloo igloo = getValidatedIgloo(id);

        // 방문자 리스트 반환
        return QuizInfoResponse.fromList(igloo.getQuizzes());
    }

    @Transactional(readOnly = true)
    public boolean isCorrect(Long quizId, Integer answer) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + quizId));

        System.out.println("Correct Answer Index: " + quiz.getCorrectAnswerIndex() + ", Given Answer: " + answer);

        return quiz.isCorrectAnswer(answer);
    }

    @UserAuthorize
    @Transactional
    public QuizDetailInfoResponse addQuiz(String token, Long id, String question, List<String> options, Integer correctAnswerIndex) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // Igloo 찾기
        Igloo igloo = getValidatedIgloo(id);

        // 소유자 여부 판단
        if (!igloo.getOwner().getId().equals(member.getId())) {
            // 소유자인 경우 예외 처리
            throw new IllegalArgumentException("You aren't the owner of this Igloo");
        }

        // Quiz 생성
        Quiz quiz = Quiz.builder()
                .igloo(igloo)
                .question(question)
                .options(options)
                .correctAnswerIndex(correctAnswerIndex)
                .build();

        // Igloo에 Quiz 추가
        igloo.addQuiz(quiz);

        // QuizDetailInfoResponse 반환
        return QuizDetailInfoResponse.from(quiz);
    }

    @Transactional
    public void deleteQuiz(String token, Long QuizId) {

        Quiz quiz = quizRepository.findById(QuizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + QuizId));

        Igloo igloo = iglooRepository.findById(quiz.getIgloo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Igloo not found"));

        Member member = memberService.getUserByToken(token);
        boolean isOwner = quiz.getIgloo().getId().equals(member.getId());

        // 소유자 여부 판단
        if (!isOwner) {
            // 소유자가 아닌 경우 예외 처리
            throw new IllegalArgumentException("You aren't the Owner of this Quiz");
        }
        // Igloo 엔티티에서 롤링페이퍼 제거
        igloo.removeQuiz(quiz);
    }

    @Transactional
    public void deleteAllQuiz(String token, Long IglooId) {


        Igloo igloo = iglooRepository.findById(IglooId)
                .orElseThrow(() -> new IllegalArgumentException("Igloo not found"));

        Member member = memberService.getUserByToken(token);
        boolean isOwner = IglooId.equals(member.getId());

        // 소유자 여부 판단
        if (!isOwner) {
            // 소유자가 아닌 경우 예외 처리
            throw new IllegalArgumentException("You aren't the Owner of this Quiz");
        }

        igloo.removeQuizzes();
    }

    @Transactional
    public QuizUpdateResponse updateQuiz(String token, Long quizId, String question, List<String> options, Integer correctAnswerIndex) {

        // 퀴즈 조회
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + quizId));

        // 사용자 인증 및 소유권 확인
        Member member = memberService.getUserByToken(token);
        if (!quiz.getIgloo().getId().equals(member.getId())) {
            throw new IllegalArgumentException("You are not the owner of this quiz.");
        }

        // 수정할 내용 확인
        if (question == null && (options == null || options.isEmpty()) && correctAnswerIndex == null) {
            throw new IllegalArgumentException("No updates provided.");
        }

        // 퀴즈 수정
        if (question != null) {
            quiz.updateQuestion(question);
        }
        if (options != null && !options.isEmpty()) {
            quiz.updateOptions(options);
        }
        if (correctAnswerIndex != null) {
            quiz.updateCorrectAnswerIndex(correctAnswerIndex);
        }

        // 수정 결과 반환
        return QuizUpdateResponse.of(true, quiz);
    }


    private Igloo getValidatedIgloo(Long id) {
        // Igloo 찾기 및 예외 처리
        return getIglooByMemberId(id)
                .orElseThrow(() -> new IllegalArgumentException("Igloo not found for this member"));
    }
}
