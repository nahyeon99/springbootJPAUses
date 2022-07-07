package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    // column이 1,2,3,4...로 들어간다.
    // 중간에 다른 상태가 만들어지면 망가지므로 EnumType의 default는 ORDINAL이므로 STRING으로 바꿔준다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP
}
