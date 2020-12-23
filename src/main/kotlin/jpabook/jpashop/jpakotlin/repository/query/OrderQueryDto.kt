package jpabook.jpashop.jpakotlin.repository.query

import jpabook.jpashop.jpakotlin.domain.Address
import jpabook.jpashop.jpakotlin.domain.OrderStatus
import java.time.LocalDateTime

class OrderQueryDto(
    var orderId: Long,
    var name: String,
    var orderDate : LocalDateTime,  //주문시간
    var orderStatus: OrderStatus,
    var address: Address,
    var orderItems: List<OrderItemQueryDto>
)