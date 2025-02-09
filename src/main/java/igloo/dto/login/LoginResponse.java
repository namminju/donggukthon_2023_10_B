package igloo.dto.login;

import common.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "회원 이름", example = "콜라곰")
        String name,
        @Schema(description = "회원 유형", example = "USER")
        MemberType type,
        @Schema(description = "회원 아이디", example = "USER")
        Long id,
        String token
) {}