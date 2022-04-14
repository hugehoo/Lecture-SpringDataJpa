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
@Transactional
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
        assertEquals(findMember, member1);

    }

}