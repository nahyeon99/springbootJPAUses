package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.awt.*;

import static org.junit.Assert.*;

// junit실행할 때 스프링이랑 엮어서 실행할래
@RunWith(SpringRunner.class)
@SpringBootTest // 스프링 부트를 띄운 상태에서 테스트를 하기 위함 (eg. autowired)
@Transactional // 데이터를 변경할 예정, rollback이 default
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

//    @Autowired EntityManager em;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long saveId = memberService.join(member);

        // then
//        em.flush(); // 영속성 컨텍스트에 있는 내용을 db에 반영(쿼리 강제로 날려버림)

        // JPA에서 같은 transaction안에서 같은 entity면(pk값이 같으면) 같은 영속성 컨텍스트로 관리
        Assertions.assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member1.setName("kim");

        // when
        memberService.join(member1);
        memberService.join(member2); // 예외 발생해야한다, 같은 이름이므로

//        expected 통해 지울 수 있음
//        try {
//            memberService.join(member2); // 예외 발생해야한다, 같은 이름이므로
//        } catch(IllegalStateException e) {
//            return;
//        }

        // then
        Assert.fail("예외가 발생해야 한다.");
    }
}