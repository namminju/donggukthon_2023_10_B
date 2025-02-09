package igloo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Setter
public class Igloo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member owner; // 이글루의 소유자

    private String introduction; // 한 줄 소개

    @ManyToMany
    @JoinTable(
            name = "visitors",
            joinColumns = @JoinColumn(name = "igloo_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )

    private List<Member> visitors = new ArrayList<>(); // 방문한 회원 리스트

    @OneToMany(mappedBy = "igloo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizRanking> quizRankings = new ArrayList<>(); // 퀴즈 랭킹 리스트

    @OneToMany(mappedBy = "igloo", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RollingPaper> rollingPapers = new ArrayList<>(); // 롤링페이퍼 리스트

    @OneToMany(mappedBy = "igloo", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Quiz> quizzes = new ArrayList<>(); // 퀴즈 리스트

    @Column(unique = true)  // 초대 코드에 대해 유니크 제약 조건 설정
    private String invitationCode; // 초대 코드

    @Builder
    private Igloo(Member owner, String introduction, List<Member> visitors) {
        this.owner = owner;
        this.introduction = introduction;
        this.quizRankings = quizRankings != null ? quizRankings : new ArrayList<>();
        this.visitors = visitors != null ? visitors : new ArrayList<>();
        this.invitationCode = generateInvitationCode(); // 초대 코드 생성
    }

    // 롤링페이퍼 추가
    public void addRollingPaper(RollingPaper rollingPaper) {
        if (rollingPaper != null) {
            rollingPaper.setIgloo(this); // 롤링페이퍼의 Igloo-> 현재 Igloo
            this.rollingPapers.add(rollingPaper);
        }
    }

    public void removeRollingPaper(RollingPaper rollingPaper) {
        if (rollingPaper != null && this.rollingPapers.contains(rollingPaper)) {
            this.rollingPapers.remove(rollingPaper); // 리스트에서 제거
            rollingPaper.setIgloo(null); // 연관 관계 해제
        }
    }


    // 퀴즈 추가
    public void addQuiz(Quiz quiz) {
        if (quiz != null) {
            quiz.setIgloo(this); // 롤링페이퍼의 Igloo-> 현재 Igloo
            this.quizzes.add(quiz);
        }
    }

    public void removeQuiz(Quiz quiz) {
        if (quiz != null && this.quizzes.contains(quiz)) {
            this.quizzes.remove(quiz); // 리스트에서 제거
            quiz.setIgloo(null); // 연관 관계 해제
        }
    }

    public void removeQuizzes() {
        this.quizzes.clear();
    }

    // 방문자 추가
    public void addVisitor(Member visitor) {
        if (visitor != null && !this.visitors.contains(visitor)) {
            this.visitors.add(visitor);
        }
    }

    // 소개 수정
    public void updateIntroduction(String introduction) {
        if (introduction != null && !introduction.isBlank()) {
            this.introduction = introduction;
        }
    }

    // 퀴즈 랭킹 추가 (중복 시 업데이트)
    public void addOrUpdateRanking(Member member, Long score) {
        QuizRanking existingRanking = quizRankings.stream()
                .filter(r -> r.getMember().equals(member))
                .findFirst()
                .orElse(null);

        if (existingRanking != null) {
            existingRanking.updateScore(score); // 기존 점수 업데이트
        } else {
            QuizRanking newRanking = QuizRanking.builder()
                    .igloo(this)
                    .member(member)
                    .score(score)
                    .timestamp(LocalDateTime.now())
                    .build();
            quizRankings.add(newRanking);
        }
    }
//    // 특정 멤버의 랭킹 삭제
//    public void removeRanking(Member member) {
//        quizRankings.removeIf(ranking -> ranking.getMember().equals(member));
//    }
//
//    // 전체 랭킹 삭제
//    public void clearRankings() {
//        quizRankings.clear();
//    }

    public List<QuizRanking> getRankingsSortedByScore() {
        return quizRankings.stream()
                .sorted(Comparator.comparing(QuizRanking::getScore).reversed()) // 점수 내림차순 정렬
                .toList();
    }

    // 6자리 고유 초대 코드 생성
    private String generateInvitationCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder invitationCodeBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            invitationCodeBuilder.append(characters.charAt(randomIndex));
        }
        return invitationCodeBuilder.toString();
    }

    @Override
    public String toString() {
        return "Igloo{" +
                "id=" + id +
                ", owner=" + (owner != null ? owner.getId() : "null") +
                ", introduction='" + introduction + '\'' +
                ", visitors=" + visitors +
                ", rollingPapers=" + rollingPapers +
                ", quizzes=" + quizzes +
                ", invitationCode='" + invitationCode + '\'' +
                '}';
    }
}
