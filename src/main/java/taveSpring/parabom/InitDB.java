package taveSpring.parabom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import taveSpring.parabom.Domain.Member;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit(){
            System.out.println("<<<<<<<<<<<  INITIALIZE DATABASE  >>>>>>>>>>>>" );
            System.out.println("====  INITIALIZE MEMBER DATABASE  ====" );
            Member member1 = createMember("email1@gmail.com","password","profile1","nickname1","address1");
            Member member2 = createMember("email2@gmail.com","password","profile2","nickname2","address2");
            Member member3 = createMember("email3@gmail.com","password","profile3","nickname3","address3");
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
        }

        private Member createMember( String email, String password
                ,String profile, String nickname, String address){
            return Member.builder()
                    .email(email)
                    .password(password)
                    .profile(profile)
                    .nickname(nickname)
                    .address(address)
                    .build();
        }

    }
}
