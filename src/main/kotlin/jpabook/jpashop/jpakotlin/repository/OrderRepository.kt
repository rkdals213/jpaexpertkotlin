package jpabook.jpashop.jpakotlin.repository

import jpabook.jpashop.jpakotlin.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order?, Long?> {
    @Query(value = "select o from Order o join fetch o.member m join fetch o.delivery d")
    fun findAllWithMemberDelivery(): List<Order?>?

    @Query(value = "select distinct o from Order o join fetch o.member m join fetch o.delivery d join fetch o.orderItems oi join fetch oi.item i")
    fun findAllWithItem(): List<Order?>?
}
