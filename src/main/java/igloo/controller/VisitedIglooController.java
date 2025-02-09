package igloo.controller;

import igloo.dto.ApiResponse;
import igloo.service.VisitedIglooService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "방문한 이글루 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/visited")
public class VisitedIglooController {
    private final VisitedIglooService visitedIglooService;

    @Operation(summary = "방문한 이글루 추가")
    @PostMapping("")
    public ApiResponse addVisitor(@RequestHeader("Authorization") String authorizationHeader,
                                  @RequestParam Long id) {  // @RequestParam 사용하여 id 받기
        String token = extractToken(authorizationHeader); // 헤더에서 토큰 추출

        return ApiResponse.success(visitedIglooService.addVisitedIgloo(token, id));
    }

    @Operation(summary = "방문한 이글루 목록")
    @GetMapping("")
    public ApiResponse visitorInfo(@RequestHeader("Authorization") String authorizationHeader) {  // @RequestParam 사용하여 id 받기
        String token = extractToken(authorizationHeader); // 헤더에서 토큰 추출

        // 방문한 이글루 목록 반환
        return ApiResponse.success(visitedIglooService.getVisitedIgloo(token));
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }
        return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값 반환
    }
}
