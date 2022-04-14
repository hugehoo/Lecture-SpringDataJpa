package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional // 이게 없으면 에러발생 -> JPA 의 모든 데이터 변경은 transactional 안에서 작동해야하기 때문
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = Member.builder().username("memberA").build();
        Member member1 = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(member1.getId());

        assertEquals(findMember.getId(), member1.getId());
        assertEquals(findMember.getUsername(), member1.getUsername());
        assertEquals(findMember, member1); // 동일 트랜잭션 안에서는 같은 인스턴스인게 보장된다.

    }

}