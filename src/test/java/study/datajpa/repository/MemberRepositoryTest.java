package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Rollback(value = false)
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Data Jpa 테스트")
    public void testMember() {
        Member member = Member.builder().username("memberA").build();
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(member, savedMember);


    }

    @Test
    @DisplayName("숫수 JPA 기반의 코드를 spring data jpa 에서 그대로 사용할 수 있다.")
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);

        // list 조회 검증
        List<Member> all = memberRepository.findAll();
        assertEquals(all.size(), 2);

        // count 검증
        long count = memberRepository.count();
        assertEquals(count, 2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertEquals(deletedCount, 0);

    }

    @Test
    @DisplayName("custom data jpa repository")
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("AAB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 10);
        assertEquals(aaa.get(0).getUsername(), "AAA");
        assertEquals(aaa.get(0).getAge(), 20);
        assertEquals(aaa.size(), 1);

    }

    @Test
    @DisplayName("")
    public void testQuery() {
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("AAB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findUser("AAA", 20);
        assertEquals(aaa.get(0).getUsername(), "AAA");
        assertEquals(aaa.get(0).getAge(), 20);
        assertEquals(aaa.size(), 1);

    }

    @Test
    @DisplayName("column 조회하기")
    public void findUserNameList() {
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("AAB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> aaa = memberRepository.findUsername();
        for (String s : aaa) {
            System.out.println("s = " + s);
        }
    }


    @Test
    @DisplayName("DTO 조회하기")
    public void findMemberDto() {
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member m1 = new Member("AAA", 20);
        m1.setTeam(teamA);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    @DisplayName("DTO 조회하기")
    public void findMemberDto2() {
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);


        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);
        }
    }


    @Test
    @DisplayName("DTO 조회하기")
    public void returnType() {
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);


        List<Member> aaa = memberRepository.findListByUsername("AAA");
        Member aaa1 = memberRepository.findMemberByUsername("AAA");
        Optional<Member> aaa2 = memberRepository.findOptionalByUsername("AAA");

        System.out.println(aaa);
        System.out.println(aaa1);
        System.out.println(aaa2);

    }

    @Test
    @DisplayName("Paging 처리 + DTO 조회하기")
    public void paging() {

        // given
        memberRepository.save(new Member("AAA1", 20));
        memberRepository.save(new Member("AAA2", 20));
        memberRepository.save(new Member("AAA3", 20));
        memberRepository.save(new Member("AAA4", 20));
        memberRepository.save(new Member("AAA5", 20));

        int age = 20;

        // Pageable 을 상속한 클래스
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // When
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        // Page 를 유지하면서 DTO 로 변환시켜 컨트롤러에 노출하자 (엔티티 노출 금지)
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();


        // for Page
        assertEquals(content.size(), 3);
        assertEquals(page.getTotalElements(), 5);
        assertEquals(page.getNumber(), 0);
        assertEquals(page.getTotalPages(), 2);
        assertTrue(page.isFirst());
        assertTrue(page.hasNext());

//        // for Slice
//        assertEquals(content.size(), 3);
//        assertEquals(page.getNumber(), 0);
//        assertTrue(page.isFirst());
//        assertTrue(page.hasNext());
    }

    @Test
    @DisplayName("bulk update test")
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 50));

        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush(); // 영속 컨텍스트의 남아있는 변경되지 않는 사항들을 저장한다 (DB 에)
        em.clear(); // 영속 컨텍스트 날린다.

        Member member5 = memberRepository.findMemberByUsername("member5");
        System.out.println("member5 = " + member5); // 51이 아닌 50으로 나온다.
        assertEquals(resultCount, 4);
    }

    @Test
    public void findMemberLazy() {
        // given
        // member1 -> teamA
        // member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }

        List<Member> member11 = memberRepository.findByUsername("member1");
        assertEquals(member11.get(0).getTeam(), teamA);

        em.flush();
        em.clear();

    }
}