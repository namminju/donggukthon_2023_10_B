package igloo.service;

import igloo.domain.Igloo;
import igloo.domain.Member;
import igloo.dto.Igloo.IglooInfoResponse;
import igloo.dto.Igloo.IglooVisitorInfoResponse;
import igloo.repository.IglooRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IglooService {
    private final IglooRepository iglooRepository;
    private final MemberService memberService;

    public Optional<Igloo> getIglooByMemberId(Long memberId) {
        return iglooRepository.findByOwnerId(memberId); // Member의 id로 Igloo 찾기
    }

    @Transactional(readOnly = true)
    public IglooInfoResponse getIglooInfo(String token, Long Id) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // Igloo 찾기
        Optional<Igloo> iglooOptional = getIglooByMemberId(Id);

        // Igloo가 존재하지 않는 경우 예외 처리
        if (iglooOptional.isEmpty()) {
            throw new IllegalArgumentException("Igloo not found for this member");
        }

        Igloo igloo = iglooOptional.get();

        // 소유자 여부 판단
        boolean isOwner = igloo.getOwner().getId().equals(member.getId());

        // IglooInfoResponse로 변환하여 반환 (소유자 여부 추가)
        return IglooInfoResponse.from(igloo, isOwner);
    }

    @Transactional(readOnly = true)
    public List<IglooInfoResponse> findIgloo(String token, String invitationCode) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // 일부 초대 코드가 포함된 이글루를 찾기
        List<Igloo> igloos = iglooRepository.findByInvitationCodeContaining(invitationCode);

        // Igloo가 존재하지 않는 경우 예외 처리
        if (igloos.isEmpty()) {
            throw new IllegalArgumentException("Igloo not found for this invitation code");
        }

        // 소유자 여부 판단 후 IglooInfoResponse로 변환하여 반환 (소유자 여부 추가)
        return igloos.stream()
                .map(igloo -> IglooInfoResponse.from(igloo, igloo.getOwner().getId().equals(member.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public IglooInfoResponse updateIntroduction(String token, Long Id, String introduction) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // Igloo 찾기
        Optional<Igloo> iglooOptional = getIglooByMemberId(Id);

        // Igloo가 존재하지 않는 경우 예외 처리
        if (iglooOptional.isEmpty()) {
            throw new IllegalArgumentException("Igloo not found for this member");
        }

        Igloo igloo = iglooOptional.get();

        // 소유자 여부 판단
        if (!igloo.getOwner().getId().equals(member.getId())) {
            // 소유자가 아닌 경우 예외 처리
            throw new IllegalArgumentException("You are not the owner of this Igloo");
        }

        // 소개글 업데이트
        igloo.updateIntroduction(introduction);

        // IglooInfoResponse로 변환하여 반환
        return IglooInfoResponse.from(igloo, true); // 소유자이므로 true
    }

    @Transactional
    public ResponseEntity<String> addVisitor(String token, Long id) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // Igloo 찾기
        Optional<Igloo> iglooOptional = getIglooByMemberId(id);

        // Igloo가 존재하지 않는 경우 예외 처리
        if (iglooOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Igloo not found for this member");
        }

        Igloo igloo = iglooOptional.get();

        // 소유자 여부 판단
        if (igloo.getOwner().getId().equals(member.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are the owner of this Igloo");
        }

        // 방문자 추가
        igloo.addVisitor(member);

        // 방문 성공 응답
        return ResponseEntity.status(HttpStatus.CREATED).body("Visitor added successfully");
    }

    @Transactional(readOnly = true)
    public List<IglooVisitorInfoResponse> getIglooVisitors(Long id) {

        // Igloo 찾기
        Optional<Igloo> iglooOptional = getIglooByMemberId(id);

        // Igloo가 존재하지 않는 경우 예외 처리
        if (iglooOptional.isEmpty()) {
            throw new IllegalArgumentException("Igloo not found for this member");
        }

        Igloo igloo = iglooOptional.get();

        // 방문자 리스트 반환
        return IglooVisitorInfoResponse.fromList(igloo.getVisitors());
    }



}
