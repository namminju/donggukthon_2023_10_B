package igloo.service;

import igloo.domain.Igloo;
import igloo.domain.Member;
import igloo.dto.Igloo.IglooInfoResponse;
import igloo.repository.IglooRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VisitedIglooService {

    private final MemberService memberService;
    private final IglooRepository iglooRepository;

    // 메소드 이름을 좀 더 직관적으로 수정
    public Optional<Igloo> findIglooById(Long id) {
        return iglooRepository.findById(id); // Igloo ID로 찾기
    }

    @Transactional
    public ResponseEntity<String> addVisitedIgloo(String token, Long id) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // Igloo 찾기
        Optional<Igloo> iglooOptional = findIglooById(id);

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
        member.addVisitedIgloo(igloo); // 이 메소드가 Member 클래스에 정의되어 있어야 합니다.

        // 방문 성공 응답
        return ResponseEntity.status(HttpStatus.CREATED).body("Visitor added successfully");
    }

    @Transactional(readOnly = true)
    public List<IglooInfoResponse> getVisitedIgloo(String token) {

        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // 방문한 이글루 목록 반환
        return IglooInfoResponse.from(member.getVisitedIgloos(), false);
    }
}
