package igloo.controller;

import igloo.dto.ApiResponse;
import igloo.service.QuizRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "퀴즈 랭킹 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz/ranking")
public class QuizRankingController {
    private final QuizRankingService quizRankingService;

    @Operation(summary = "퀴즈 랭킹 저장")
    @PatchMapping("/update")
    public ApiResponse updateIntroduction(@RequestHeader("Authorization") String authorizationHeader,
                                          @RequestParam Long id,
                                          @RequestBody Long score) {  // @RequestParam 사용하여 id 받기
        String token = extractToken(authorizationHeader); // 헤더에서 토큰 추출
        // 이글루 정보 반환
        quizRankingService.updateQuizRanking(token, id, score);
        return ApiResponse.success("Successfully added quiz ranking.");
    }

    @Operation(summary = "퀴즈 랭킹 목록")
    @GetMapping("")
    public ApiResponse QuizRankingsInfo(@RequestParam Long id) {  // @RequestParam 사용하여 id 받기
        // 이글루 방문자 목록 반환
        return ApiResponse.success(quizRankingService.getQuizRankings(id));
    }

    @Operation(summary = "나의 퀴즈 랭킹 목록")
    @GetMapping("/mine")
    public ApiResponse MemberQuizRanking(@RequestHeader("Authorization") String authorizationHeader,
                                         @RequestParam Long iglooId) {  // @RequestParam 사용하여 id 받기

        String token = extractToken(authorizationHeader); // 헤더에서 토큰 추출

        // 이글루 방문자 목록 반환
        return ApiResponse.success(quizRankingService.getMemberQuizRanking(token, iglooId));
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }
        return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값 반환
    }
}
