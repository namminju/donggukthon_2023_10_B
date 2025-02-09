package igloo.dto.quiz;


import igloo.domain.Quiz;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record QuizUpdateResponse(
        @Schema(description = "롤링페이퍼 수정 성공 여부", example = "true")
        boolean result,
        @Schema(description = "퀴즈 고유키", example = "1")
        Long id,
        @Schema(description = "문제", example = "내 키는?")
        String question,
        @Schema(description = "보기", example = "[\"155\",\"157\"]")
        List<String> options,
        @Schema(description = "답", example = "1")
        Integer correctAnswerIndex
) {
    public static QuizUpdateResponse of(boolean result, Quiz quiz) {
        return new QuizUpdateResponse(result, quiz.getId(), quiz.getQuestion(),quiz.getOptions(),
                quiz.getCorrectAnswerIndex());
    }
}
