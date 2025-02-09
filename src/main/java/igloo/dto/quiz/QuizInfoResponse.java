package igloo.dto.quiz;

import igloo.domain.Quiz;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

public record QuizInfoResponse(
        @Schema(description = "퀴즈 고유키", example = "1")
        Long id,
        @Schema(description = "문제", example = "내 키는?")
        String question,
        @Schema(description = "보기", example = "[\"보기1\", \"보기2\", \"보기3\"]")
        List<String> options,
        @Schema(description = "정답", example = "1")
        Integer correctAnswerIndex

) {
        // RollingPaper 객체를 RollingPaperInfoResponse로 변환하는 from 메서드
        public static QuizInfoResponse from(Quiz quiz) {
                return new QuizInfoResponse(
                        quiz.getId(),
                        quiz.getQuestion(),
                        quiz.getOptions(),
                        quiz.getCorrectAnswerIndex()
                );
        }

        // RollingPaper 리스트를 변환하는 메서드 (stream 사용)
        public static List<QuizInfoResponse> fromList(List<Quiz> quizzes) {
                return quizzes.stream()
                        .map(QuizInfoResponse::from)
                        .collect(Collectors.toList());
        }
}
