package igloo.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "회원 아이디", example = "admin")
        String account,
        @Schema(description = "회원 비밀번호", example = "admin")
        String password
) {
}
