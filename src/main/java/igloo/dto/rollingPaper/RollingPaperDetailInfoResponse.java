package igloo.dto.rollingPaper;

import igloo.domain.RollingPaper;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record RollingPaperDetailInfoResponse(
        @Schema(description = "롤링페이퍼 고유키", example = "1")
        Long id,
        @Schema(description = "메세지", example = "안녕!")
        String message,
        @Schema(description = "작성자", example = "나비")
        String writer,
        @Schema(description = "회원 생성일", example = "2023-05-11T15:00:00")
        LocalDateTime updatedAt,
        @Schema(description = "작성자 여부", example = "true")
        Boolean isWriter
) {
        public static RollingPaperDetailInfoResponse from(RollingPaper rollingPaper, boolean isWriter) {
                return new RollingPaperDetailInfoResponse(
                        rollingPaper.getId(),
                        rollingPaper.getMessage(),
                        rollingPaper.getWriter().getNickname(),
                        rollingPaper.getUpdatedAt(),
                        isWriter
                );
        }

        public static List<RollingPaperDetailInfoResponse> fromList(List<RollingPaper> rollingPapers) {
                List<RollingPaperDetailInfoResponse> responses = new ArrayList<>();
                for (RollingPaper rollingPaper : rollingPapers) {
                        boolean isOwner = rollingPaper.getWriter().getId().equals(rollingPaper.getIgloo().getId());
                        responses.add(from(rollingPaper, isOwner));
                }
                return responses;
        }
}
