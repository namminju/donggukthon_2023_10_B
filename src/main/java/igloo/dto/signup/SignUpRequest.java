package igloo.dto.signup;

import common.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;


public record SignUpRequest(
        @Schema(description = "회원 아이디", example = "test")
        String account,

        @Schema(description = "회원 비밀번호", example = "1234")
        String password,

        @Schema(description = "회원 이름", example = "홍길동")
        String name,

        @Schema(description = "회원 별명", example = "의적")
        String nickname,

        @Schema(hidden = true)
        MemberType type
) {
        // 기본값을 USER로 설정하는 생성자
        public SignUpRequest {
                if (type == null) {
                        type = MemberType.USER;  // type이 null이면 USER로 설정
                }
        }
}
