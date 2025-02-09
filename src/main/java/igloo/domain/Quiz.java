package igloo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Setter
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "igloo_id", nullable = false)
    private Igloo igloo;

    @Column(nullable = false, length = 500)
    private String question;

    @ElementCollection
    @CollectionTable(name = "quiz_options", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "option", nullable = false)
    private List<String> options;

    @Column(nullable = false)
    private Integer correctAnswerIndex;

    @Builder
    public Quiz(Igloo igloo, String question, List<String> options, Integer correctAnswerIndex) {
        if (options == null) {
            throw new IllegalArgumentException("Quiz must have options.");
        }
        if (correctAnswerIndex < 0 || correctAnswerIndex >= options.size()) {
            throw new IllegalArgumentException("Correct answer index must be between 0 and 4.");
        }
        this.igloo = igloo;
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void updateQuestion(String newQuestion) {
        if (newQuestion != null && !newQuestion.isBlank()) {
            this.question = newQuestion;
        }
    }

    public void updateOptions(List<String> newOptions) {
        if (newOptions != null && !newOptions.isEmpty()) {
            this.options = newOptions;
        }
    }

    public void updateCorrectAnswerIndex(Integer newCorrectAnswerIndex) {
        if (newCorrectAnswerIndex != null && newCorrectAnswerIndex >= 0 && newCorrectAnswerIndex < this.options.size()) {
            this.correctAnswerIndex = newCorrectAnswerIndex;
        }
    }

    // 정답 확인 메서드
    public boolean isCorrectAnswer(int answerIndex) {
        return this.correctAnswerIndex != null && this.correctAnswerIndex.equals(answerIndex);
    }
}
