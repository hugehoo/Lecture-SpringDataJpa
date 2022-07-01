package study.datajpa.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"id", "username", "age"})
@Data
public class Member extends BaseEntity {

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.age = age;
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.age = age;
        this.username = username;
        this.team = team;
    }

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private int age;
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
