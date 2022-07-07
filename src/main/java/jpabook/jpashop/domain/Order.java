package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // Order 입장에서는 다:1
    @JoinColumn(name = "member_id") // FK 설정, 연관관계의 주인
    private Member member;
//    private Member member = new ByteBuddyInterceptor(); // 프록시 기술이 자동으로 적용되어있음

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

//    CascadeType.ALL 기능
//    persist(orderItemA) // default는 entity 당 각자 영속화를 해주어야 함
//    persist(orderItemB)
//    persist(orderItemC)
//    persist(order)
//
//    설정시
//    persist(order) // 전부 영속화 가능

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) // order까지 전부 persist 해줌
    @JoinColumn(name = "delivery_id") // 연관관계의 주인
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상세 [ORDER, CANCLE]

    // === 연관관계 메서드 === // 원자적으로 양뱡향 setting 할 수 있게 해준다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비지니스 로직==//
    /*
    * 주문 취소
     */
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        // JPA를 활용하면 데이터가 바뀌면(status가 바뀌면) JPA가 알아서 Dirty Checking(변경내역 감지)를 해서
        // 변경된 내역들을 데이터베이스에 Update를 해준다. (JPA의 강점)
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 같이 주문한 item들 취소처리
        }
    }
    //==조회 로직==//
    /*
    * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
