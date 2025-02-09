package igloo.dto.Igloo;

import io.swagger.v3.oas.annotations.media.Schema;

public record IglooUpdateRequest(
        @Schema(description = "변경하고자 하는 소개글", example = "안녕하세요")
        String introduction


) {
}
