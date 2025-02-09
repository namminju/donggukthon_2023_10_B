package igloo.dto.rollingPaper;


import igloo.domain.RollingPaper;
import io.swagger.v3.oas.annotations.media.Schema;

public record RollingPaperUpdateResponse(
        @Schema(description = "롤링페이퍼 수정 성공 여부", example = "true")
        boolean result,
        @Schema(description = "메세지", example = "안녕?")
        String name,
        @Schema(description = "작성자", example = "홍길동")
        String nickname
) {
    public static RollingPaperUpdateResponse of(boolean result, RollingPaper rollingPaper) {
        return new RollingPaperUpdateResponse(result, rollingPaper.getMessage(), rollingPaper.getWriter().getNickname());
    }
}
