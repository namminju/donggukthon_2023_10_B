package igloo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RollingPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "igloo_id", nullable = false)
    private Igloo igloo;

    @Column(nullable = false, length = 500)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    @Column(nullable = false)
    private Integer type;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public RollingPaper(Igloo igloo, String message, Member writer, Integer type) {
        this.igloo = igloo;
        this.message = message;
        this.writer = writer;
        this.type = type;
    }

    public void updateMessage(String newMessage) {
        if (newMessage != null && !newMessage.isBlank()) {
            this.message = newMessage;
        }
    }

    public void updateType(Integer newType) {
        if (newType != null) {
            this.type = newType;
        }
    }
    // Igloo와의 연관 관계 설정 메서드
    public void setIgloo(Igloo igloo) {
        this.igloo = igloo;
    }
}
