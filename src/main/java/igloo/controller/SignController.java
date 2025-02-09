package igloo.controller;

import igloo.dto.ApiResponse;
import igloo.dto.login.LoginRequest;
import igloo.dto.signup.SignUpRequest;
import igloo.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "회원 가입 & 로그인 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class SignController {
    private final SignService signService;

    @Operation(summary = "회원 가입")
    @PostMapping("/register")
    public ApiResponse signUp(@RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registMember(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        return ApiResponse.success(signService.login(request));
    }

    @Operation(summary = "아이디 중복 체크")
    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkUsername(String id) {
        Map<String, Object> response = new HashMap<>();

        // 아이디 중복 확인
        boolean isExists = signService.checkIfAccountExists(id);

        response.put("exists", isExists);

        if (isExists) {
            response.put("message", "이미 사용 중인 아이디입니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "사용 가능한 아이디입니다.");
            return ResponseEntity.ok(response);
        }
    }


}
