package igloo.dto.rollingPaper;

import igloo.domain.RollingPaper;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

public record RollingPaperInfoResponse(
        @Schema(description = "롤링페이퍼 고유키", example = "1")
        Long id,
        @Schema(description = "타입", example = "1")
        Integer type
) {
        // RollingPaper 객체를 RollingPaperInfoResponse 로 변환하는 from 메서드
        public static RollingPaperInfoResponse from(RollingPaper rollingPaper) {
                return new RollingPaperInfoResponse(
                        rollingPaper.getId(),
                        rollingPaper.getType()
                );
        }

        // RollingPaper 리스트를 변환하는 메서드 (stream 사용)
        public static List<RollingPaperInfoResponse> fromList(List<RollingPaper> rollingPapers) {
                return rollingPapers.stream()
                        .map(RollingPaperInfoResponse::from)
                        .collect(Collectors.toList());
        }
}
