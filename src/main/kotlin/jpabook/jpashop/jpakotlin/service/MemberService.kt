package jpabook.jpashop.jpakotlin.service

import jpabook.jpashop.jpakotlin.domain.Member
import jpabook.jpashop.jpakotlin.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private var memberRepository: MemberRepository
) {

    fun findMembers(): MutableList<Member?> {
        return memberRepository.findAll()
    }

    fun join(member: Member): Long? {
        return memberRepository.save(member).id
    }

    fun findOne(id: Long): Member {
        return memberRepository.findById(id).get()
    }

    @Transactional
    fun update(id: Long, name: String?) {
        val member: Member = findOne(id)
        member.name = name
    }
}
