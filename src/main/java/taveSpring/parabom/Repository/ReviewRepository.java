package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taveSpring.parabom.Domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
