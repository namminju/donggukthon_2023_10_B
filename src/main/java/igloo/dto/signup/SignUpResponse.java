package igloo.dto.signup;

import igloo.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignUpResponse(
        @Schema(description = "회원 고유키", example = "1")
        Long id,
        @Schema(description = "회원 아이디", example = "admin")
        String account,
        @Schema(description = "회원 이름", example = "콜라곰")
        String name,
        @Schema(description = "회원 별명", example = "나비")
        String nickname
) {
    public static SignUpResponse from(Member member) {
        return new SignUpResponse(
                member.getId(),
                member.getAccount(),
                member.getName(),
                member.getNickname()
        );
    }
}
