package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if(item.getId() == null) { // item은 JPA에 저장되기 전까지는 Id 값이 없음 => 새로 생성한 객체
            em.persist(item);

        } else { // 이미 DB에 등록된 것이므로 update 느낌
            // Item merge = em.merge(item);
            // merge는 영속성 컨텍스트가 O, 파라미터로 넘어온 item은 영속성 컨텍스트로 변하진 않는다.
            // dirty checking은 원하는 값만 update할 수 있다면, merge는 모든 값을 업데이트 해버리는 치명적인 단점이 있다.
            // 위험!! 병합(merge)시 값이 없으면 데이터베이스에 null 로 업데이트 해버린다.
            // 그래서 실무에서는 merge보다는 dirty checking(변경감지) 해야한다.
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
