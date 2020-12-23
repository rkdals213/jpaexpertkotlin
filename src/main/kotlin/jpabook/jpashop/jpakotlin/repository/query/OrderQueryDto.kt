package jpabook.jpashop.jpakotlin.repository.query

import jpabook.jpashop.jpakotlin.domain.Address
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

    // map으로 그루핑할때 기준점이 됨
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderQueryDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}