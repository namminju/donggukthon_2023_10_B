package igloo.dto.quizRanking;

import igloo.domain.QuizRanking;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record QuizRankingResponse(
        @Schema(description = "퀴즈 랭킹 수정 성공 여부", example = "true")
        boolean result,

        @Schema(description = "점수", example = "30")
        Long score,

        @Schema(description = "닉네임", example = "홍길동")
        String nickname,

        @Schema(description = "생성일", example = "2024-02-01T12:34:56")
        LocalDateTime dateTime
) {
    // 단건 반환
    public static QuizRankingResponse of(QuizRanking quizRanking) {
        return new QuizRankingResponse(
                true, // 성공 여부
                quizRanking.getScore(), // 점수
                quizRanking.getMember().getNickname(), // 닉네임
                quizRanking.getTimestamp() // 생성일
        );
    }

    // 리스트 반환
    public static List<QuizRankingResponse> of(List<QuizRanking> quizRankings) {
        return quizRankings.stream()
                .map(QuizRankingResponse::of)
                .collect(Collectors.toList());
    }
}
