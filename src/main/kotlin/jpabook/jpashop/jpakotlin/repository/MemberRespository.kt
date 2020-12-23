package jpabook.jpashop.jpakotlin.repository

import jpabook.jpashop.jpakotlin.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member?, Long?> {
    abstract fun save(member: Member?): Member
}
