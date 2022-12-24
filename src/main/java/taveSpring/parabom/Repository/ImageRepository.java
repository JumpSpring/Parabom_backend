package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Post;

import java.util.List;


public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("FROM Image i JOIN Post p WHERE p.id = ?1")
    List<Image> findByPostId(Long id); // 게시물 id로 해당 게시물 이미지리스트 불러오기

}
