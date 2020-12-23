package jpabook.jpashop.jpakotlin.repository.query

import jpabook.jpashop.jpakotlin.domain.Address
import jpabook.jpashop.jpakotlin.domain.OrderStatus
import java.time.LocalDateTime

class OrderFlatDto(
    var id: Long,
    var name: String, //주문시간
    var orderDate: LocalDateTime,
    var status: OrderStatus,
    var address: Address,
    var itemName: String,
    var orderPrice: Int,
    var count: Int
)