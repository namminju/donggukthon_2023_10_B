package igloo.service;

import igloo.config.security.UserAuthorize;
import igloo.domain.Igloo;
import igloo.domain.Member;
import igloo.domain.RollingPaper;
import igloo.dto.Igloo.IglooInfoResponse;
import igloo.dto.rollingPaper.RollingPaperDetailInfoResponse;
import igloo.dto.rollingPaper.RollingPaperInfoResponse;
import igloo.dto.rollingPaper.RollingPaperUpdateResponse;
import igloo.repository.IglooRepository;
import igloo.repository.RollingPaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@UserAuthorize
@RequiredArgsConstructor
@Service
public class RollingPaperService {
    private final IglooRepository iglooRepository;
    private final MemberService memberService;
    private final RollingPaperRepository rollingPaperRepository;

    public Optional<Igloo> getIglooByMemberId(Long memberId) {
        return iglooRepository.findByOwnerId(memberId); // Member의 id로 Igloo 찾기
    }

    @Transactional(readOnly = true)
    public List<RollingPaperInfoResponse> getRollingPapers(Long id) {
        // Igloo 찾기
        Igloo igloo = getValidatedIgloo(id);

        // 방문자 리스트 반환
        return RollingPaperInfoResponse.fromList(igloo.getRollingPapers());
    }

    @UserAuthorize
    @Transactional(readOnly = true)
    public List<RollingPaperDetailInfoResponse> getDetailRollingPapers(String token, Long id) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // Igloo 찾기
        Igloo igloo = getValidatedIgloo(id);

        // 소유자 여부 판단
        if (!igloo.getOwner().getId().equals(member.getId())) {
            // 소유자가 아닌 경우 예외 처리
            throw new IllegalArgumentException("You are not the owner of this Igloo");
        }

        // 롤링페이퍼 반환
        return RollingPaperDetailInfoResponse.fromList(igloo.getRollingPapers());
    }

    @UserAuthorize
    @Transactional(readOnly = true)
    public RollingPaperDetailInfoResponse getDetailRollingPaper(String token, Long rollingPaperId) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // 롤링페이퍼 검색
        RollingPaper rollingPaper = rollingPaperRepository.findById(rollingPaperId)
                .orElseThrow(() -> new IllegalArgumentException("Rolling paper does not exist."));

        // 소유자 여부 확인
        boolean isOwner = rollingPaper.getIgloo().getId().equals(member.getId());
        boolean isWriter = rollingPaper.getWriter().getId().equals(member.getId());

        if (!isOwner && !isWriter) {
            throw new IllegalArgumentException("You do not have permission to this rolling paper.");
        }

        // 롤링페이퍼 반환
        return RollingPaperDetailInfoResponse.from(rollingPaper, isWriter);
    }

    @UserAuthorize
    @Transactional
    public RollingPaperDetailInfoResponse addRollingPapers(String token, Long id, String message, Integer type) {
        // 토큰으로 사용자 검색
        Member member = memberService.getUserByToken(token);

        // Igloo 찾기
        Igloo igloo = getValidatedIgloo(id);

        // 소유자 여부 판단
        if (igloo.getOwner().getId().equals(member.getId())) {
            // 소유자인 경우 예외 처리
            throw new IllegalArgumentException("You are the owner of this Igloo");
        }

        // RollingPaper 생성
        RollingPaper rollingPaper = RollingPaper.builder()
                .igloo(igloo)
                .message(message)
                .writer(member)
                .type(type)
                .build();

        // Igloo에 RollingPaper 추가
        igloo.addRollingPaper(rollingPaper);

        // RollingPaperDetailInfoResponse 반환
        return RollingPaperDetailInfoResponse.from(rollingPaper, false);
    }

    @Transactional
    public void deleteRollingPaper(String token, Long rollingPaperId) {

        RollingPaper rollingPaper = rollingPaperRepository.findById(rollingPaperId)
                .orElseThrow(() -> new IllegalArgumentException("RollingPaper not found with id: " + rollingPaperId));

        Igloo igloo = iglooRepository.findById(rollingPaper.getIgloo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Igloo not found"));

        Member member = memberService.getUserByToken(token);
        boolean isWriter = rollingPaper.getWriter().getId().equals(member.getId());

        // 소유자 여부 판단
        if (!isWriter) {
            // 소유자가 아닌 경우 예외 처리
            throw new IllegalArgumentException("You aren't the writer of this Rolling Paper");
        }
        // Igloo 엔티티에서 롤링페이퍼 제거
        igloo.removeRollingPaper(rollingPaper);
    }

    @Transactional
    public RollingPaperUpdateResponse updateRollingPaper(String token, Long rollingPaperId, String newMessage, Integer newType) {

        RollingPaper rollingPaper = rollingPaperRepository.findById(rollingPaperId)
                .orElseThrow(() -> new IllegalArgumentException("RollingPaper not found with id: " + rollingPaperId));

        Member member = memberService.getUserByToken(token);
        boolean isWriter = rollingPaper.getWriter().getId().equals(member.getId());

        // 소유자 여부 판단
        if (!isWriter) {
            // 소유자가 아닌 경우 예외 처리
            throw new IllegalArgumentException("You aren't the writer of this Rolling Paper");
        }
        // 수정할 내용이 없는 경우 예외 처리
        if (newMessage == null && newType == null) {
            throw new IllegalArgumentException("No updates provided. Both message and type are null.");
        }
        // 롤링페이퍼 수정
        if (newMessage != null) {
            rollingPaper.updateMessage(newMessage);
        }
        if (newType != null) {
            rollingPaper.updateType(newType);
        }
        return RollingPaperUpdateResponse.of(true, rollingPaper);
    }

    private Igloo getValidatedIgloo(Long id) {
        // Igloo 찾기 및 예외 처리
        return getIglooByMemberId(id)
                .orElseThrow(() -> new IllegalArgumentException("Igloo not found for this member"));
    }
}
