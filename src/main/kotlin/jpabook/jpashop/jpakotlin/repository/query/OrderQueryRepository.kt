package jpabook.jpashop.jpakotlin.repository.query

import org.springframework.stereotype.Repository
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.persistence.EntityManager

@Repository
class OrderQueryRepository(
    private val em: EntityManager
) {

    fun findOrderQueryDtos(): List<OrderQueryDto> {
        //루트 조회(toOne 코드를 모두 한번에 조회)
        val result = findOrders()
        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
        result.forEach(Consumer { o: OrderQueryDto ->
            val orderItems: List<OrderItemQueryDto> = findOrderItems(o.orderId)
            o.orderItems = orderItems
        })
        return result
    }

    /**
     * 1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
     */
    private fun findOrders(): List<OrderQueryDto> {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                    " from Order o" +
                    " join o.member m" +
                    " join o.delivery d", OrderQueryDto::class.java
        ).resultList
    }

    /**
     * 1:N 관계인 orderItems 조회
     */
    private fun findOrderItems(orderId: Long): List<OrderItemQueryDto> {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                    " from OrderItem oi" +
                    " join oi.item i" +
                    " where oi.order.id = : orderId",
            OrderItemQueryDto::class.java
        )
            .setParameter("orderId", orderId)
            .resultList
    }

    fun findAllByDto_optimization(): List<OrderQueryDto> {
        //루트 조회(toOne 코드를 모두 한번에 조회)
        val result = findOrders()
        //orderItem 컬렉션을 MAP 한방에 조회
        val orderItemMap = findOrderItemMap(toOrderIds(result))
        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행X)
        result.forEach(Consumer { o: OrderQueryDto ->
            o.orderItems = orderItemMap.get(o.orderId)!!
        })
        return result
    }

    private fun toOrderIds(result: List<OrderQueryDto>): MutableList<Any>? {
        return result.stream()
            .map<Any>(OrderQueryDto::orderId)
            .collect(Collectors.toList<Any>())
    }

    private fun findOrderItemMap(orderIds: MutableList<Any>?): Map<Long, List<OrderItemQueryDto>> {
        val orderItems = em.createQuery(
            ("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                    " from OrderItem oi" +
                    " join oi.item i" +
                    " where oi.order.id in :orderIds"), OrderItemQueryDto::class.java
        ).setParameter("orderIds", orderIds).resultList


        return orderItems.groupBy(OrderItemQueryDto::orderId)
    }

    fun findAllByDto_flat(): List<OrderFlatDto> {
        return em.createQuery(
            ("select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                    " from Order o" +
                    " join o.member m" +
                    " join o.delivery d" +
                    " join o.orderItems oi" +
                    " join oi.item i"), OrderFlatDto::class.java
        ).resultList
    }
}