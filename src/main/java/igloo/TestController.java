package igloo;

import igloo.config.security.UserAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "테스트 API")
@RequiredArgsConstructor
@RestController
public class TestController {

    @Operation(summary = "토큰 필요 API")
    @UserAuthorize// 권한 확인
    @GetMapping("/isToken")
    public String check() {
        return "test";
    }
    
    @Operation(summary = "동작 확인 API")
    @GetMapping("/isRunning")
    public String run() {
        return "true";
    }
}
