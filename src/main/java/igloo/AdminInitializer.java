package igloo;

import common.MemberType;
import igloo.domain.Member;
import igloo.dto.signup.SignUpRequest;
import igloo.dto.signup.SignUpResponse;
import igloo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AdminInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;	// 추가

    @Override
    public void run(ApplicationArguments args) {
        SignUpRequest signUpRequest = new SignUpRequest(
                "admin",                // account
                "admin",                // password
                "관리자",
                "god",// name
                MemberType.ADMIN        // type
        );

        registMember(signUpRequest);
    }

    @Transactional
    public SignUpResponse registMember(SignUpRequest request) {
        Member member = memberRepository.save(Member.from(request, encoder));	// 회원가입 정보를 암호화하도록 수정
        try {
            memberRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        return SignUpResponse.from(member);
    }
}
