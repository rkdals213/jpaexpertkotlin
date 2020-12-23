package jpabook.jpashop.jpakotlin.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import jpabook.jpashop.jpakotlin.domain.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import javax.annotation.Resource
import javax.persistence.EntityManager

@Repository
class OrderRepository2(
    private val em: EntityManager,
    @Qualifier("JpaQueryFactory") private val query: JPAQueryFactory
) {

    fun findAllWithMemberDelivery(@Param("offset") offset: Int, @Param("limit") limit: Int): List<Order> {
        return em.createQuery(
            "select distinct o from Order o" +
                    " join fetch o.member m" +
                    " join fetch o.delivery d" +
                    " join fetch o.orderItems oi" +
                    " join fetch oi.item i", Order::class.java
        )
            .setFirstResult(offset)
            .setMaxResults(limit)
            .resultList
    }

    fun findAll(orderSearch: OrderSearch): MutableList<Order>? {
        val order: QOrder = QOrder.order
        val member: QMember = QMember.member
        return query
            .select(order)
            .from(order)
            .join(order.member, member)
            .where(
                statusEq(orderSearch.orderStatus),
                nameLike(orderSearch.memberName)
            )
            .limit(1000)
            .fetch()
    }

    private fun statusEq(statusCond: OrderStatus?): BooleanExpression? {
        return if (statusCond == null) {
            null
        } else QOrder.order.status.eq(statusCond)
    }

    private fun nameLike(nameCond: String?): BooleanExpression? {
        return if (!StringUtils.hasText(nameCond)) {
            null
        } else QMember.member.name.like(nameCond)
    }
}