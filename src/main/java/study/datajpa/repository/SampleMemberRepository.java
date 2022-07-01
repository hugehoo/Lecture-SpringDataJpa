package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.SampleMember;

public interface SampleMemberRepository extends JpaRepository<SampleMember, Long> {
}
