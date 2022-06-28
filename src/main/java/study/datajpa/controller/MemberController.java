package study.datajpa.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // id가 아닌, 도메인 컨버터 클래스. (갠적으로 권장하지 않는다 -> pk 를 드러내고, 너무 간단한 경우에만 사용할 수 있기 때문)
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
//    public Page<Member> list(Pageable pageable) {
    public Page<MemberDto> list(@PageableDefault(size=5) Pageable pageable) { // application.yml 설정보다 우선한다.
        Page<Member> all = memberRepository.findAll(pageable);
//        return all.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getUsername()));
        return all.map(MemberDto::new);
    }

//    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
