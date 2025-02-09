package igloo.controller;

import igloo.config.security.UserAuthorize;
import igloo.dto.ApiResponse;
import igloo.dto.quiz.AddQuizRequest;
import igloo.dto.quiz.QuizUpdateRequest;
import igloo.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "퀴즈 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizService quizService;

    @Operation(summary = "퀴즈 조회")
    @GetMapping("")
    public ApiResponse quizInfo(@RequestParam Long id) {  // @RequestParam 사용하여 id 받기
        // 이글루 방문자 목록 반환
        return ApiResponse.success(quizService.getQuizzes(id));
    }

    @UserAuthorize
    @Operation(summary = "퀴즈 추가")
    @PostMapping("/add")
    public ApiResponse addQuiz(@RequestHeader("Authorization") String authorizationHeader,
                                       @RequestParam Long id,
                                       @RequestBody AddQuizRequest request) {  // @RequestParam 사용하여 id 받기
        // 이글루 방문자 목록 반환
        String token = extractToken(authorizationHeader);

        return ApiResponse.success(quizService.addQuiz(token, id, request.question(), request.options(), request.correctAnswerIndex()));
    }


    @UserAuthorize
    @Operation(summary = "퀴즈 제거")
    @DeleteMapping("delete/quiz/{quizId}")
    public ApiResponse deleteQuiz(@RequestHeader("Authorization") String authorizationHeader,
                                          @PathVariable Long quizId) {
        String token = authorizationHeader.replace("Bearer ", "");

        // 서비스 호출
        quizService.deleteQuiz(token, quizId);

        return ApiResponse.success("Rolling paper deleted successfully");
    }

    @UserAuthorize
    @Operation(summary = "이글루에 연결된 모든 퀴즈 삭제")
    @DeleteMapping("delete/quizzes/{iglooId}")
    public ApiResponse deleteAllQuizzes(@RequestHeader("Authorization") String authorizationHeader,
                                        @PathVariable Long iglooId) {
        // 토큰에서 "Bearer " 제거
        String token = authorizationHeader.replace("Bearer ", "").trim();

        // 퀴즈 삭제 서비스 호출
        quizService.deleteAllQuiz(token, iglooId);

        // 성공 응답 반환
        return ApiResponse.success("All quizzes associated with the Igloo have been successfully deleted.");
    }

    @UserAuthorize
    @Operation(summary = "퀴즈 수정")
    @PatchMapping("/{quizId}")
    public ApiResponse updateQuiz(@RequestHeader("Authorization") String authorizationHeader,
                                   @PathVariable Long quizId,
                                   @RequestBody QuizUpdateRequest request) {
        String token = authorizationHeader.replace("Bearer ", "");

        return ApiResponse.success(quizService.updateQuiz(token, quizId, request.getQuestion(),request.getOptions(),request.getCorrectAnswerIndex()));
    }

    @UserAuthorize
    @Operation(summary = "퀴즈 정답 여부")
    @GetMapping("/{quizId}")
    public ApiResponse checkQuizAnswer(
            @PathVariable Long quizId,
            @RequestParam Integer correctAnswerIndex) {

        return ApiResponse.success(quizService.isCorrect(quizId, correctAnswerIndex));
    }


    // Authorization 헤더에서 Bearer 토큰 추출
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }
        return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값 반환
    }
}
