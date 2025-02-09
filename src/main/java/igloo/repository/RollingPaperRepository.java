package igloo.repository;

import igloo.domain.Igloo;
import igloo.domain.RollingPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RollingPaperRepository extends JpaRepository<RollingPaper, Long> {
    Optional<RollingPaper> findById(Long id);
}
