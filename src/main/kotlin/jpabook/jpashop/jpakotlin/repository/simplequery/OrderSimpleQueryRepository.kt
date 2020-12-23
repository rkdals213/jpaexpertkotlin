package jpabook.jpashop.jpakotlin.repository.simplequery

import jpabook.jpashop.jpakotlin.domain.Order
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class OrderSimpleQueryRepository(
    private val em: EntityManager
) {
    fun findOrderDtos(): MutableList<OrderSimpleQueryDto?> {
        return em.createQuery(
            "select new\n" +
                    "jpabook.jpashop.jpakotlin.repository.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)\n" +
                    "from Order o join o.member m\n" +
                    "join o.delivery d", OrderSimpleQueryDto::class.java
        )
            .resultList
    }
}