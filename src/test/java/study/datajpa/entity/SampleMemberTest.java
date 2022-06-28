package study.datajpa.entity;

import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.repository.SampleMemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SampleMemberTest {

    @Autowired
    SampleMemberRepository sampleMemberRepository;

    @Test
    public void test() {
        SampleMember member1 = new SampleMember("member1");
        sampleMemberRepository.save(member1);

        List<SampleMember> sampleMembers = sampleMemberRepository.findAll();
        for (SampleMember sampleMember : sampleMembers) {
            System.out.println("[ sampleMember.getCreateDate() ] : " + sampleMember.getCreateDate());
            System.out.println("[ sampleMember.getLastModifiedDate() ] : " + sampleMember.getLastModifiedDate());
        }
    }
}