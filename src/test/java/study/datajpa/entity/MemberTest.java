package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;



@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {


    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = Team.builder()
                .name("teamA").build();
        Team teamB = Team.builder()
                .name("teamB").build();

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = Member.builder()
                .username("member1")
                .age(10)
                .team(teamA)
                .build();

        Member member2 = Member.builder()
                .username("member1")
                .age(20)
                .team(teamA)
                .build();


        Member member3 = Member.builder()
                .username("member1")
                .age(30)
                .team(teamB)
                .build();


        Member member4 = Member.builder()
                .username("member1")
                .age(40)
                .team(teamB)
                .build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush(); // 강제로 insert 쿼리를 다 날려버린다.
        em.clear();

        // 확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        for (Member member : members) {
            System.out.println(member.toString());
            System.out.println(member.getTeam());
        }
    }

    @Test
    public void JpaEventBaseEntity() throws Exception{
        // given
        Member member1 = new Member("member1");
        memberRepository.save(member1); // @PrePersist 발생

        Thread.sleep(1000);
        member1.setUsername("member2");

        em.flush(); // @PreUpdate
        em.clear();

        // when
        Member findMember = memberRepository.findById(member1.getId()).get();

        // then
        System.out.println("findMember.getCreatedDate() = " + findMember.getCreateDate());
        System.out.println("findMember.getLastModifiedDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());


    }
}