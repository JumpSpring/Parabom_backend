package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.PostRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /*게시물 상세조회*/
    @Transactional
    public PostDto.PostDetailDto productDetail(Long id) {
        Post entity = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        return new PostDto.PostDetailDto(entity);
    }

    /*게시물 등록*/
    @Transactional
    public Long postCreate(PostDto.PostCreateDto dto) {
        return postRepository.save(dto.toEntity()).getId();
    }

}
