package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 직접 쿼리를 작성 :  위의 메서드에 비해 메서드명이 짧기때문에 심플하다. : 오타가 있어도 컴파일 오류로 잡아준다(장점)
    // 이름이 없는 named query 로 볼 수 있다. (강사 추천 막강한 기능)
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsername();

    // new 를 써줘야하는 번거로움이 있네. -> 결국 나중에 QDsl 사용한다.
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name)  from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);

    Member findMemberByUsername(String username);

    Optional<Member> findOptionalByUsername(String aaa);

    // count query 를 분리 -> count query 를 연산할 때는 조인하지 않고 count 만 수행하도록 설정하여 성능 최적화
    // 이걸 분리하지 않으면 count query 연산할 때도 조인하기 때문에 성능이 떨어진다.
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
  //    Slice<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)  // Update 쿼리인데 @Modifying 이 없으면 예외발생 // cleanAutomatically = true -> em.clear
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Override
    @EntityGraph(attributePaths = {"team"}) // 기존 findAll 을 오버라이드하여, fetch join 까지 적용시킨 것.
    List<Member> findAll();

    // TODO : 적용해보기
    @EntityGraph(attributePaths = {"team"}) // 회원 데이터를 쓸 때 team 데이터까지 뽑아야할 상황이면 유용하게 쓸 수 있따.
    List<Member> findByUsername(@Param("username") String username);

}
