package taveSpring.parabom.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import javax.transaction.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostService postService;

    //Post post1 = getPost("ps5", 500000, 0, )

    private Post getPost(String name, int price, Integer foi, Date datePurchased, Integer openOrNot,
                         String status, String directOrDel, String category, String hashtag,
                         String title, String content, Member member) {
        return Post.createPost(name, price, foi, datePurchased, openOrNot, status, directOrDel,
                category, hashtag, title, content, member);
    }

    @Test
    public void 게시글_상세조회() throws Exception {
        //given

        //when

        //then
    }

}