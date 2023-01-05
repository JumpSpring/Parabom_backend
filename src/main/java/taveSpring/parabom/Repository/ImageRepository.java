package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import taveSpring.parabom.Domain.Image;

import java.util.List;


public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i JOIN i.post p WHERE p.id = ?1")
    List<Image> findByIdBy(Long id); // 게시물 id로 해당 게시물 이미지리스트 불러오기

    Image findByOriFileName(String oriFileName);
}
