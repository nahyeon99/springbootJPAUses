package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // @Component로 Component scan의 대상이 되서 스프링빈으로 자동 등록된다.
@RequiredArgsConstructor
public class MemberRepository {

    // Spring이 JPA의 EntityManager를 주입해준다.
    // em은 @autowired가 아닌 @PersistenceContext가 있어야 injection이 된다.
    // But, 스프링이 @autowired도 주입할 수 있게 지원해준다.
    // @PersistenceContext
    private final EntityManager em;
//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    public void save(Member member) {
        em.persist(member);
        // 영속성 컨텍스트에 member 엔티티를 넣는다.
        // 트랜잭션이 커밋되는 시점에 디비에 반영된다.
    }

    public Member findOne(Long id) { // JPA의 find 메소드 사용, 단건 조회
        return em.find(Member.class, id); // (타입, pk)
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class) // JPQL, FROM의 대상이 테이블이 아닌, entity 에 대한 쿼리
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) // parameter 바인딩
                .getResultList();
    }
}
