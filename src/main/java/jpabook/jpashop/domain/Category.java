package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private long id;

    private String name;


    @ManyToMany
    // 다:다 테이블은 1:다, 다:1로 풀어내야한다.
    // CATEGORY_ITEM 테이블로 Item 테이블과 Category 테이블의 중간매핑을 해준다.
    // 실무에선 필드 추가가 안되므로 사용하지 않는다.
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    // ✅ @XToOne 의 모든 연관관계는 지연로딩으로 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Category> child = new ArrayList<>();

    // === 연관관계 메서드 === // 원자적으로 양뱡향 setting 할 수 있게 해준다.

    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
