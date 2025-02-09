package igloo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class QuizRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "igloo_id", nullable = false)
    private Igloo igloo; // 랭킹이 속한 Igloo

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 랭킹에 등록된 사용자

    private Long score; // 점수
    private LocalDateTime timestamp; // 기록된 시간

    @Builder
    public QuizRanking(Igloo igloo, Member member, Long score, LocalDateTime timestamp) {
        this.igloo = igloo;
        this.member = member;
        this.score = score;
        this.timestamp = timestamp;
    }

    // 점수 갱신
    public void updateScore(Long newScore) {
        this.score = newScore;
        this.timestamp = LocalDateTime.now(); // 갱신 시간 업데이트
    }
}
