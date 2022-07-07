package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.transform.Result;
import java.util.List;
import java.util.stream.Collectors;

// @Controller + @ResponseBody // Rest API 스타일
// @ResponseBody 데이터 자체를 json 이나 xml로 보내자
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한 경우

    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    // 조회 V2

    @GetMapping("/api/v2/members")
    public Result memberV2() {

        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    /**
     * 회원 등록 API
     */
    // @RequestBody: JSON(예시)으로 온 데이터를 알아서 매핑해서 넣어준다.
    // entity를 직접적으로 쓰게되면 큰 장애가 많이 발생하므로, 별도의 dto로 parameter로 받아야 한다.
    // api로 쓸 때는 entity를 외부에 노출해서도 안된다.

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor // 모든 파라미터를 넘기는 생성자
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    /**
     * dto
     * entity를 변경하더라도 api spec을 변경하지 않을 수 있고,
     * api spec을 한 눈에 확인할 수 있으며 참고해서 feat하게 작성할 수 있다.
     * parameter로 Member entity를 받기보다는 별도의 dto를 받아서 작업하는 것이 api 방식의 정석
     * api는 요청을 들어오고 나올 때 절대 엔티티가 아닌 dto를 통해 주고 받는다!
     */

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
