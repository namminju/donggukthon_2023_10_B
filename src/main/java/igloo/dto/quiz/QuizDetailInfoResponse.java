package igloo.dto.quiz;

import igloo.domain.Quiz;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public record QuizDetailInfoResponse(
        @Schema(description = "퀴즈 고유키", example = "1")
        Long id,
        @Schema(description = "문제", example = "내 키는?")
        String question,
        @Schema(description = "보기", example = "[\"155\",\"157\"]")
        List<String> options,
        @Schema(description = "답", example = "1")
        Integer correctAnswerIndex
) {
        public static QuizDetailInfoResponse from(Quiz quiz) {
                return new QuizDetailInfoResponse(
                        quiz.getId(),
                        quiz.getQuestion(),
                        quiz.getOptions(),
                        quiz.getCorrectAnswerIndex()
                );
        }

        public static List<QuizDetailInfoResponse> fromList(List<Quiz> quizzes) {
                List<QuizDetailInfoResponse> responses = new ArrayList<>();
                for (Quiz quiz : quizzes) {
                        responses.add(from(quiz));
                }
                return responses;
        }
}
