package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // readOnly면 저장 안됨, overriding
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantiry) {
        Item findItem = itemRepository.findOne(itemId); // findItem은 영속성 상태
        // 변경 감지도 setter보다는 change()와 같은 의미있는 메소드를 만들고 역추적할 수 있게끔 하자. (엔티티 레벨에서)
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantiry);
        // 트랜잭션이 커밋이 되면 JPA는 flush()를 날려서 영속성 컨텍스트에 변경된 애들을 다 수정해서 merge 해서 update 해준다.
        // 변경감지에 의해 데이터를 변경하는 방법

    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
