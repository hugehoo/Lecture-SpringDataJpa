package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

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

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);

        // list 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertEquals(all.size(), 2);

        // count 검증
        long count = memberJpaRepository.count();
        assertEquals(count, 2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        assertEquals(deletedCount, 0);

    }



}