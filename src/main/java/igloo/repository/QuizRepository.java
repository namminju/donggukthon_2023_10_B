package igloo.repository;

import igloo.domain.Quiz;
import igloo.domain.RollingPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findById(Long id);
}
