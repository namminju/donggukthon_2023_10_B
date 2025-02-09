package igloo.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberUpdateRequest(
        @Schema(description = "회원 비밀번호", example = "1234")
        String password,
        @Schema(description = "회원 새 비밀번호", example = "1234")
        String newPassword,
        @Schema(description = "회원 이름", example = "홍길동")
        String name,
        @Schema(description = "회원 새 닉네임", example = "의적")
        String nickname

) {
}
