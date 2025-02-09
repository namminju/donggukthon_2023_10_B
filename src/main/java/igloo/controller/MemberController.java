package igloo.controller;

import igloo.config.security.UserAuthorize;
import igloo.dto.ApiResponse;
import igloo.dto.member.MemberPasswordUpdateRequest;
import igloo.dto.member.MemberUpdateRequest;
import igloo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@UserAuthorize
@Tag(name = "회원 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class MemberController {
    private final MemberService memberService;

    // 회원 정보 조회
    @Operation(summary = "회원 정보 조회")
    @GetMapping("/info")
    public ApiResponse getMemberInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // Bearer 토큰에서 실제 토큰 값 추출
        String token = extractToken(authorizationHeader);

        // 서비스로 토큰 전달하여 회원 정보 조회
        return ApiResponse.success(memberService.getMemberInfo(token));
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/delete")
    public ApiResponse deleteMember(@RequestHeader("Authorization") String authorizationHeader) {
        // Bearer 토큰에서 실제 토큰 값 추출
        String token = extractToken(authorizationHeader);

        // 서비스로 토큰 전달하여 회원 탈퇴
        return ApiResponse.success(memberService.deleteMember(token));
    }

    // 회원 정보 수정
    @Operation(summary = "회원 정보 수정")
    @PutMapping("/update")
    public ApiResponse updateMember(@RequestHeader("Authorization") String authorizationHeader,
                                    @RequestBody MemberUpdateRequest request) {
        // Bearer 토큰에서 실제 토큰 값 추출
        String token = extractToken(authorizationHeader);

        // 서비스로 토큰 전달하여 회원 정보 수정
        return ApiResponse.success(memberService.updateMember(token, request));
    }

    @Operation(summary = "회원 별명 수정")
    @PatchMapping("/update/nickname")
    public ApiResponse updateNickname(@RequestHeader("Authorization") String authorizationHeader,
                                      String newNickname) {
        // Bearer 토큰에서 실제 토큰 값 추출
        String token = extractToken(authorizationHeader);

        // 서비스로 토큰 전달하여 회원 별명 수정
        return ApiResponse.success(memberService.updateNickname(token, newNickname));
    }

    @Operation(summary = "회원 비밀번호 수정")
    @PatchMapping("/update/password")
    public ApiResponse updatePassword(@RequestHeader("Authorization") String authorizationHeader,
                                      @RequestBody MemberPasswordUpdateRequest request) {
        // Bearer 토큰에서 실제 토큰 값 추출
        String token = extractToken(authorizationHeader);

        // 서비스로 토큰 전달하여 회원 비밀번호 수정
        return ApiResponse.success(memberService.updatePassword(token, request.password(), request.newPassword()));
    }


    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     *
     * @param authorizationHeader Authorization 헤더 값
     * @return Bearer 이후의 실제 토큰 값
     */
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }
        return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값 반환
    }
}
