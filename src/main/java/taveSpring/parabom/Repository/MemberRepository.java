package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import taveSpring.parabom.Domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    //회원 중복 방지
    boolean existsByEmail(String email);



}
