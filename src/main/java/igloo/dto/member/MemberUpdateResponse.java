package igloo.dto.member;

import igloo.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberUpdateResponse(
        @Schema(description = "회원 정보 수정 성공 여부", example = "true")
        boolean result,
        @Schema(description = "회원 이름", example = "콜라곰")
        String name,
        @Schema(description = "회원 별명", example = "나비")
        String nickname
) {
    public static MemberUpdateResponse of(boolean result, Member member) {
        return new MemberUpdateResponse(result, member.getName(), member.getNickname());
    }
}
