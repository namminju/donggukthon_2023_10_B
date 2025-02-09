package igloo.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberPasswordUpdateRequest(
        @Schema(description = "회원 비밀번호", example = "1234")
        String password,
        @Schema(description = "회원 새 비밀번호", example = "1234")
        String newPassword

) {
}
