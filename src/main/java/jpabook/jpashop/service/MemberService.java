package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Component scan의 대상이 되어서 스프링 빈에 자동 등록
@Transactional(readOnly = true) // transaction 안에서 데이터 변경되어야 할 때 필요, readOnly 시 JPA가 성능을 더 최적화 가능
//@AllArgsConstructor // 생성자 자동 생성
@RequiredArgsConstructor // final이 있는 필드만 가지고 생성자를 만들어준다.
public class MemberService {

//    @Autowired // Spring이 스프링빈에 등록되어있는 repository를 injection 해준다. (필드 injection)
    // 이 경우 접근도, test시 변경도 어려워서 생성자 injection을 많이 사용한다.
    private final MemberRepository memberRepository; // final로 생성자의 파라미터 입력 안했을 때 컴파일 오류로 체크할 수 있음

    // @Autowired // 생성자가 하나인 경우 auto injection
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /*
    * 회원 가입
     */
    @Transactional // default 쓰기 기능 추가
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
