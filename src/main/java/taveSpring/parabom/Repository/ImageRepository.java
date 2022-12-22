package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taveSpring.parabom.Domain.Image;


public interface ImageRepository extends JpaRepository<Image, Long> {

}
