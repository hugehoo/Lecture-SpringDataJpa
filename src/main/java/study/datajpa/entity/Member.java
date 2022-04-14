package study.datajpa.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString(of ={"id", "username", "age"}) // team 적으면 큰일난다. 연관관계 필드는 tosTring 안하는것이 좋다.
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); // team 에 있는 member 에게도 setting 을 해줘야한다. 객체이기 때문에 양쪽에서 바꿔줘야한다.
    }
}
