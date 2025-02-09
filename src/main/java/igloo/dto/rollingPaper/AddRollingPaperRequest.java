package igloo.dto.rollingPaper;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddRollingPaperRequest(
        @Schema(description = "내용", example = "안녕하세요")
        String message,
        @Schema(description = "타입", example = "1")
        Integer type


) {
}
