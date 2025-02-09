package igloo.service;

import igloo.config.security.TokenProvider;
import igloo.domain.Member;
import igloo.dto.login.LoginRequest;
import igloo.dto.login.LoginResponse;
import igloo.dto.signup.SignUpRequest;
import igloo.dto.signup.SignUpResponse;
import igloo.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;

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

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByAccount(request.account())
                .filter(it -> encoder.matches(request.password(), it.getPassword()))	// 암호화된 비밀번호와 비교하도록 수정
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        String token = tokenProvider.createToken(String.format("%s:%s", member.getAccount(), member.getType()));
        return new LoginResponse(member.getName(), member.getType(), member.getId(), token);
    }
    // 아이디 존재 여부 확인
    @Transactional(readOnly = true)
    public boolean checkIfAccountExists(String account) {
        return memberRepository.findByAccount(account).isPresent();
    }



}
