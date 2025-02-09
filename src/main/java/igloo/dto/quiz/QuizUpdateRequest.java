package igloo.dto.quiz;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizUpdateRequest {
        @Schema(description = "문제", example = "내 키는?")
        private String question;
        @Schema(description = "보기", example = "[\"155\",\"157\"]")
        List<String> options;
        @Schema(description = "답", example = "1")
        private Integer correctAnswerIndex;
}