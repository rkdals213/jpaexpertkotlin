package jpabook.jpashop.jpakotlin.repository.simplequery

import jpabook.jpashop.jpakotlin.domain.Address
import jpabook.jpashop.jpakotlin.domain.OrderStatus
import java.time.LocalDateTime

class OrderSimpleQueryDto(
    var orderId: Long,
    var name: String, //주문시간
    var orderDate: LocalDateTime,
    orderStatus: OrderStatus,
    address: Address
) {
    var orderStatus: OrderStatus = orderStatus
    var address: Address = address
}