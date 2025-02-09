package igloo.domain;

import common.MemberType;
import igloo.dto.member.MemberUpdateRequest;
import igloo.dto.signup.SignUpRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false, scale = 20, unique = true)
    private String account;

    @Column(nullable = false)
    private String password;

    private String name;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL) // 수정된 부분: cascade 추가
    private Igloo ownedIgloo;  // Member가 소유한 Igloo

    @ManyToMany
    @JoinTable(
            name = "visited_igloos",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "igloo_id")
    )
    private List<Igloo> visitedIgloos = new ArrayList<>();  // 방문한 Igloo 리스트

    @CreationTimestamp
    private LocalDateTime createdAt;

    // SignUpRequest에서 Member 엔티티로 변환하는 메소드
    public static Member from(SignUpRequest request, PasswordEncoder encoder) {
        Igloo newIgloo = Igloo.builder()
                .introduction("")  // 초기 한줄 소개
                .build();

        // Member 생성 및 Igloo 설정
        Member member = Member.builder()
                .account(request.account())
                .password(encoder.encode(request.password()))
                .name(request.name())
                .nickname(request.nickname())
                .type(MemberType.USER)
                .ownedIgloo(newIgloo)  // Member와 Igloo 연결
                .build();

        // Igloo와 연결된 Member를 설정하여 양방향 관계 설정
        newIgloo.setOwner(member);

        return member;
    }

    @Builder
    private Member(String account, String password, String name, String nickname, MemberType type, Igloo ownedIgloo, List<Igloo> visitedIgloos) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.type = type;
        this.ownedIgloo = ownedIgloo;
        this.visitedIgloos = visitedIgloos != null ? visitedIgloos : new ArrayList<>();
    }

    // Igloo를 소유하도록 설정
    public void setOwnedIgloo(Igloo igloo) {
        this.ownedIgloo = igloo;
        igloo.setOwner(this); // 양방향 관계 설정
    }

    // 방문한 Igloo 추가
    public void addVisitedIgloo(Igloo igloo) {
        if (igloo != null && !this.visitedIgloos.contains(igloo)) {
            this.visitedIgloos.add(igloo);
            igloo.addVisitor(this);  // 방문한 Igloo에 방문자 추가
        }
    }

    // 업데이트 메소드에서 비밀번호 변경 확인 로직 개선
    public void update(MemberUpdateRequest newMember, PasswordEncoder encoder) {
        this.password = newMember.newPassword() == null || newMember.newPassword().isBlank()
                ? this.password : encoder.encode(newMember.newPassword());
        this.name = newMember.name();
        this.nickname = newMember.nickname();
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updatePassword(String newPassword, PasswordEncoder encoder) {
        this.password = encoder.encode(newPassword);
    }
}
