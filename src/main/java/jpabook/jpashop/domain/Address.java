package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable // JPA의 내장객체
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // JPA 스펙상 필요한거라고 판단하고 함부로 생성하지 않게끔 설계(protected)
    // public 생성자를 사용하게끔 유도
    // 아무 것도 없는 생성자가 필요한 이유는 (protected까지 허용)
    // (=JPA가 제약을 둔 이유) JPA 구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용할 수 있도록 지원해야 하기 때문
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
