package igloo.dto.Igloo;

import igloo.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record IglooVisitorInfoResponse(
        @Schema(description = "회원 고유키", example = "1")
        Long id,
        @Schema(description = "회원 아이디", example = "colabear754")
        String account,
        @Schema(description = "회원 이름", example = "콜라곰")
        String name,
        @Schema(description = "회원 별명", example = "나비")
        String nickname,
        @Schema(description = "회원 생성일", example = "2023-05-11T15:00:00")
        LocalDateTime createdAt
) {
        // Member 객체를 IglooVisitorInfoResponse로 변환하는 from 메서드
        public static IglooVisitorInfoResponse from(Member member) {
                return new IglooVisitorInfoResponse(
                        member.getId(),
                        member.getAccount(),
                        member.getName(),
                        member.getNickname(),
                        member.getCreatedAt()
                );
        }

        // 방문자 리스트를 변환하는 메서드
        public static List<IglooVisitorInfoResponse> fromList(List<Member> visitors) {
                List<IglooVisitorInfoResponse> responses = new ArrayList<>();
                for (Member visitor : visitors) {
                        responses.add(from(visitor));
                }
                return responses;
        }
}
