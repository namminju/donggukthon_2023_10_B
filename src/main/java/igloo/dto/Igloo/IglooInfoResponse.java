package igloo.dto.Igloo;

import igloo.domain.Igloo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

public record IglooInfoResponse(
        @Schema(description = "이글루 고유키", example = "c0a80121-7aeb-4b4b-8b0a-6b1c032f0e4a")
        Long id,
        @Schema(description = "이글루 소개글", example = "안녕하세요")
        String introduction,
        @Schema(description = "이글루 소유자", example = "owner")
        String nickname,
        @Schema(description = "초대코드", example = "A8S5QP")
        String code,
        @Schema(description = "소유자 여부", example = "true")
        boolean isOwner // 소유자 여부 추가
) {
    public static IglooInfoResponse from(Igloo igloo, boolean isOwner) {
        return new IglooInfoResponse(
                igloo.getId(),
                igloo.getIntroduction(),
                igloo.getOwner().getNickname(),
                igloo.getInvitationCode(),
                isOwner // 소유자 여부 설정
        );
    }

    public static List<IglooInfoResponse> from(List<Igloo> igloos, boolean isOwner) {
        return igloos.stream()
                .map(igloo -> from(igloo, isOwner)) // IglooInfoResponse::from 사용
                .collect(Collectors.toList());
    }
}
