package study.datajpa.entity;

import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter
public class SampleMember extends BaseEntity {

    public SampleMember() {
    }

    public SampleMember(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}

