package jpabook.jpashop.jpakotlin.api

import jpabook.jpashop.jpakotlin.domain.Member
import jpabook.jpashop.jpakotlin.service.MemberService
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
class MemberController(
    private var memberService: MemberService
) {

    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한
     * 문제점
     * 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * 기본적으로 엔티티의 모든 값이 노출된다.
     * 응답 스펙을 맞추기 위해 로직이 추가된다. (@JsonIgnore, 별도의 뷰 로직 등등)
     * 실무에서는 같은 엔티티에 대해 API가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의
     * API를 위한 프레젠테이션 응답 로직을 담기는 어렵다.
     * 엔티티가 변경되면 API 스펙이 변한다.
     * 추가로 컬렉션을 직접 반환하면 항후 API 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로
     * 해결)
     * 결론
     * API 응답 스펙에 맞추어 별도의 DTO를 반환한다.
     */
    @GetMapping("/heartbeat")
    fun heartbeat(): String {
        return "heart beat"
    }

    @GetMapping("/api/v1/members")
    fun membersV1(): List<Member?>? {
        return memberService.findMembers()
    }

    @GetMapping("/api/v2/members")
    fun membersV2(): Result? {
        val findMembers: List<Member?> = memberService.findMembers()
        val collect = findMembers.stream()
            .map { m: Member? -> MemberDto(m?.name) }
            .collect(Collectors.toList())
        return Result(collect.size, collect)
    }

    class Result(var size: Int, var collect: MutableList<MemberDto>)

    class MemberDto(var name: String?)

    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.
     * 문제점
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등)
     * - 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위
     * 한 모든 요청 요구사항을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변한다.
     * 결론
     * - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
     */
    @PostMapping("/api/v1/members")
    fun saveMemberV1(@RequestBody member: Member?): CreateMemberResponse? {
        val id = memberService.join(member!!)
        return CreateMemberResponse(id)
    }

    /**
     * 등록 V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
     */
    @PostMapping("/api/v2/members")
    fun saveMemberV2(@RequestBody request: CreateMemberRequest): CreateMemberResponse? {
        val member = Member()
        member.name = request.name
        val id = memberService.join(member)
        return CreateMemberResponse(id!!)
    }

    class CreateMemberRequest(var name: String)

    class CreateMemberResponse(var id: Long?)

    /**
     * 수정 API
     */
    @PutMapping("/api/v1/members/{id}")
    fun updateMemberV1(@PathVariable("id") id: Long?, @RequestBody request: UpdateMemberRequest): UpdateMemberResponse? {
        memberService.update(id!!, request.name)
        val findMember = memberService.findOne(id)
        return UpdateMemberResponse(findMember.id, findMember.name)
    }

    class UpdateMemberRequest(var name: String?)

    class UpdateMemberResponse(var id: Long?, var name: String?)

}