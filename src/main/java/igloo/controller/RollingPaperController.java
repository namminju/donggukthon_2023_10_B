package igloo.controller;

import igloo.config.security.UserAuthorize;
import igloo.dto.ApiResponse;
import igloo.dto.rollingPaper.AddRollingPaperRequest;
import igloo.dto.rollingPaper.RollingPaperUpdateRequest;
import igloo.service.RollingPaperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "롤링페이퍼 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rollingPaper")
public class RollingPaperController {
    private final RollingPaperService rollingPaperService;


    @Operation(summary = "롤링페이퍼 조회")
    @GetMapping("")
    public ApiResponse rollingPaperInfo(@RequestParam Long id) {  // @RequestParam 사용하여 id 받기
        // 이글루 방문자 목록 반환
        return ApiResponse.success(rollingPaperService.getRollingPapers(id));
    }

    @UserAuthorize
    @Operation(summary = "롤링페이퍼 세부 조회")
    @GetMapping("/detail")
    public ApiResponse rollingPaperDetailsInfo(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestParam Long id) {  // @RequestParam 사용하여 id 받기
        // 이글루 방문자 목록 반환
        String token = extractToken(authorizationHeader);

        return ApiResponse.success(rollingPaperService.getDetailRollingPapers(token, id));
    }

    @UserAuthorize
    @Operation(summary = "롤링페이퍼 세부 조회")
    @GetMapping("/detail/{id}")  // @PathVariable 사용
    public ApiResponse rollingPaperDetailInfo(@RequestHeader("Authorization") String authorizationHeader,
                                              @PathVariable("id") Long rollingPaperId) { // @PathVariable 로 id 받기
        // Authorization 헤더에서 토큰 추출
        String token = extractToken(authorizationHeader);

        // 롤링페이퍼 정보 반환
        return ApiResponse.success(rollingPaperService.getDetailRollingPaper(token, rollingPaperId));
    }

    @UserAuthorize
    @Operation(summary = "롤링페이퍼 추가")
    @PostMapping("/add")
    public ApiResponse addRollingPaper(@RequestHeader("Authorization") String authorizationHeader,
                                       @RequestParam Long id,
                                       @RequestBody AddRollingPaperRequest request) {  // @RequestParam 사용하여 id 받기
        // 이글루 방문자 목록 반환
        String token = extractToken(authorizationHeader);

        return ApiResponse.success(rollingPaperService.addRollingPapers(token, id, request.message(), request.type()));
    }


    @UserAuthorize
    @Operation(summary = "롤링페이퍼 제거")
    @DeleteMapping("/{rollingPaperId}")
    public ApiResponse deleteRollingPaper(@RequestHeader("Authorization") String authorizationHeader,
                                          @PathVariable Long rollingPaperId) {
        String token = authorizationHeader.replace("Bearer ", "");

        // 서비스 호출
        rollingPaperService.deleteRollingPaper(token, rollingPaperId);

        return ApiResponse.success("Rolling paper deleted successfully");
    }

    @UserAuthorize
    @Operation(summary = "롤링페이퍼 수정")
    @PatchMapping("/{rollingPaperId}")
    public ApiResponse updateRollingPaper(@RequestHeader("Authorization") String authorizationHeader,
                                   @PathVariable Long rollingPaperId,
                                   @RequestBody RollingPaperUpdateRequest request) {
        String token = authorizationHeader.replace("Bearer ", "");

        return ApiResponse.success(rollingPaperService.updateRollingPaper(token, rollingPaperId, request.getMessage(), request.getType()));
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }
        return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값 반환
    }
}
