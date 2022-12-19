package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taveSpring.parabom.Domain.Post;

public interface PostRepository  extends JpaRepository<Post,Long> {

}
