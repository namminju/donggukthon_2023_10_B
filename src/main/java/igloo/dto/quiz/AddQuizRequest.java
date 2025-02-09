package igloo.dto.quiz;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AddQuizRequest(
        @Schema(description = "문제", example = "안녕하세요")
        String question,

        @Schema(description = "보기 목록", example = "[\"보기1\", \"보기2\", \"보기3\"]")
        List<String> options,

        @Schema(description = "정답 인덱스", example = "1")
        Integer correctAnswerIndex
) {
        public AddQuizRequest {
                if (question == null || question.isBlank()) {
                        throw new IllegalArgumentException("Question cannot be null or blank");
                }
                if (options == null || options.isEmpty()) {
                        throw new IllegalArgumentException("Options cannot be null or empty");
                }
                if (correctAnswerIndex == null || correctAnswerIndex < 0 || correctAnswerIndex >= options.size()) {
                        throw new IllegalArgumentException("CorrectAnswerIndex must be within the range of options");
                }
        }
}
