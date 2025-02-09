package igloo.dto.rollingPaper;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RollingPaperUpdateRequest {
        @Schema(description = "변경 메세지", example = "수정된 메세지")
        private String message;
        @Schema(description = "변경할 타입", example = "1")
        private Integer type;
}