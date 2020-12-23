package jpabook.jpashop.jpakotlin.repository.query

import jpabook.jpashop.jpakotlin.domain.Address
import jpabook.jpashop.jpakotlin.domain.OrderItem
import jpabook.jpashop.jpakotlin.domain.OrderStatus
import java.time.LocalDateTime
import java.util.ArrayList

class OrderQueryDto(
    var id: Long,
    var name: String,
    var orderDate : LocalDateTime,  //주문시간
    var status: OrderStatus,
    var address: Address
) {
    var orderItems: List<OrderItemQueryDto> = ArrayList<OrderItemQueryDto>()

    constructor(id: Long, name: String, orderDate: LocalDateTime, status: OrderStatus, address: Address, orderItems: List<OrderItemQueryDto>) : this(id, name, orderDate, status, address) {
        this.orderItems = orderItems
    }
}