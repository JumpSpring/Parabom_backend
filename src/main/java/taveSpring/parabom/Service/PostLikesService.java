package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Domain.PostLikes;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.PostLikesRepository;
import taveSpring.parabom.Repository.PostRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikesService {

    private final PostLikesRepository postLikesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Optional<PostLikes> findOne(Long memberId, Long postId) {
        return postLikesRepository.findOneByMemberAndPost(memberId, postId);
    }

    public void clickPostLikes(Long memberId, Long postId) {
        Optional<PostLikes> postLikes = postLikesRepository.findOneByMemberAndPost(memberId, postId);
        if(postLikes.isPresent()) {
            // 이미 좋아요 누른 경우 : 좋아요 삭제
            deletePostLikes(postLikes.get(), postId);
        } else {
            // 좋아요를 누르지 않은 경우
            savePostLikes(memberId, postId);
        }
    }

    private void savePostLikes(Long memberId, Long postId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Post> post = postRepository.findById(postId);
        PostLikes postLikes = PostLikes.createPostLikes(member.get(), post.get());
        postLikesRepository.save(postLikes);
        //post.get().getLikes().add(postLikes);
    }

    private void deletePostLikes(PostLikes postLikes, Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        //post.get().getLikes().remove(postLikes);
        postLikesRepository.delete(postLikes);
    }
}
