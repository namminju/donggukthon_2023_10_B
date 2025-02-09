package igloo.controller;

import igloo.config.security.UserAuthorize;
import igloo.dto.ApiResponse;
import igloo.dto.Igloo.IglooUpdateRequest;
import igloo.service.IglooService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이글루 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/igloo")
public class IglooController {
    private final IglooService iglooService;

    // 이글루 정보 조회
    @Operation(summary = "이글루 정보 조회")
    @GetMapping("")
    public ApiResponse getIglooInfo(@RequestHeader("Authorization") String authorizationHeader,
                                    @RequestParam Long id) {  // @RequestParam 사용하여 id 받기
        String token = extractToken(authorizationHeader); // 헤더에서 토큰 추출
        // 이글루 정보 반환
        return ApiResponse.success(iglooService.getIglooInfo(token, id));
    }

    // 이글루 검색
    @Operation(summary = "이글루 검색")
    @GetMapping("/find")
    public ApiResponse findIgloo(@RequestHeader("Authorization") String authorizationHeader,
                                    @RequestParam String invitationCode) {  // @RequestParam 사용하여 id 받기
        String token = extractToken(authorizationHeader); // 헤더에서 토큰 추출
        // 이글루 정보 반환
        return ApiResponse.success(iglooService.findIgloo(token, invitationCode));
    }

    @UserAuthorize
    @Operation(summary = "이글루 소개 변경")
    @PatchMapping("/update/introduction")
    public ApiResponse updateIntroduction(@RequestHeader("Authorization") String authorizationHeader,
                                          @RequestParam Long id,
                                          @RequestBody IglooUpdateRequest request) {  // @RequestParam 사용하여 id 받기
        String token = extractToken(authorizationHeader); // 헤더에서 토큰 추출
        // 이글루 정보 반환
        return ApiResponse.success(iglooService.updateIntroduction(token, id, request.introduction()));
    }

    @Operation(summary = "이글루 방문자 추가")
    @PostMapping("/visitors")
    public ApiResponse addVisitor(@RequestHeader("Authorization") String authorizationHeader,
                                          @RequestParam Long id) {  // @RequestParam 사용하여 id 받기
        String token = extractToken(authorizationHeader); // 헤더에서 토큰 추출
        // 이글루 정보 반환
        return ApiResponse.success(iglooService.addVisitor(token, id));
    }

    @Operation(summary = "이글루 방문자 목록")
    @GetMapping("/visitors")
    public ApiResponse visitorInfo(@RequestParam Long id) {  // @RequestParam 사용하여 id 받기
        // 이글루 방문자 목록 반환
        return ApiResponse.success(iglooService.getIglooVisitors(id));
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }
        return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값 반환
    }
}
