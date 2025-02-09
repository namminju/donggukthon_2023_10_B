package igloo.service;

import igloo.config.security.TokenProvider;
import igloo.domain.Member;
import igloo.dto.member.MemberDeleteResponse;
import igloo.dto.member.MemberInfoResponse;
import igloo.dto.member.MemberUpdateRequest;
import igloo.dto.member.MemberUpdateResponse;
import igloo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    /**
     * 토큰을 이용하여 사용자 정보를 검색합니다.
     *
     * @param token 유효한 JWT 토큰
     * @return Member 객체
     * @throws NoSuchElementException 사용자를 찾을 수 없는 경우
     */
    public Member getUserByToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // 토큰에서 subject 추출
        String account = tokenProvider.validateTokenAndGetSubject(token);
        System.out.println("Extracted account from token: " + account);

        // ":" 기준으로 분리하고 첫 번째 부분만 추출
        String accountPrefix = account.split(":")[0]; // ":" 앞 부분만 추출
        System.out.println("Extracted prefix from account: " + accountPrefix);

        // accountPrefix를 통해 사용자 검색
        return memberRepository.findByAccount(accountPrefix)
                .orElseThrow(() -> new NoSuchElementException("해당 토큰에 해당하는 사용자를 찾을 수 없습니다. Account: " + accountPrefix));
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(String token) {
        // 토큰으로 사용자 검색
        Member member = getUserByToken(token);
        return MemberInfoResponse.from(member);
    }

    @Transactional
    public MemberDeleteResponse deleteMember(String token) {
        // 토큰으로 사용자 검색
        Member member = getUserByToken(token);

        if (!memberRepository.existsById(member.getId())) {
            return new MemberDeleteResponse(false);
        }
        memberRepository.deleteById(member.getId());
        return new MemberDeleteResponse(true);
    }

    @Transactional
    public MemberUpdateResponse updateMember(String token, MemberUpdateRequest request) {
        // 토큰으로 사용자 검색
        Member member = getUserByToken(token);

        // 비밀번호 검증 후 업데이트
        if (!encoder.matches(request.password(), member.getPassword())) {
            throw new NoSuchElementException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 정보 업데이트
        member.update(request, encoder);
        return MemberUpdateResponse.of(true, member);
    }


    @Transactional
    public MemberUpdateResponse updateNickname(String token, String newNickname) {
        // 토큰으로 사용자 검색
        Member member = getUserByToken(token);

        // 닉네임 업데이트
        member.updateNickname(newNickname);

        return MemberUpdateResponse.of(true, member);
    }

    @Transactional
    public MemberUpdateResponse updatePassword(String token, String currentPassword, String newPassword) {
        // 토큰으로 사용자 검색
        Member member = getUserByToken(token);

        // 현재 비밀번호 검증
        if (!encoder.matches(currentPassword, member.getPassword())) {
            throw new NoSuchElementException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 업데이트
        member.updatePassword(newPassword, encoder);

        return MemberUpdateResponse.of(true, member);
    }

}
