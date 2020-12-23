package jpabook.jpashop.jpakotlin.domain

import java.time.LocalDateTime
import java.util.ArrayList
import javax.persistence.*

@Entity
@Table(name = "orders")
class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member : Member? = null//주문 회원


    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    var orderItems: MutableList<OrderItem> = ArrayList<OrderItem>()

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    var delivery : Delivery? = null//배송정보

    var orderDate : LocalDateTime? = null//주문시간


    @Enumerated(EnumType.STRING)
    var status : OrderStatus? = null //주문상태 [ORDER, CANCEL]


    //==연관관계 메서드==//
    @JvmName("setMember1")
    fun setMember(member: Member) {
        this.member = member
        member.orders.add(this)
    }

    fun addOrderItem(orderItem: OrderItem) {
        orderItems.add(orderItem)
        orderItem.order = this
    }

    @JvmName("setDelivery1")
    fun setDelivery(delivery: Delivery) {
        this.delivery = delivery
        delivery.order = this
    }

    companion object {
        fun createOrder(member: Member, delivery: Delivery, vararg orderItems: OrderItem): Order {
            val order = Order()
            order.setMember(member)
            order.setDelivery(delivery)
            for (orderItem in orderItems) {
                order.addOrderItem(orderItem)
            }
            order.status = OrderStatus.ORDER
            order.orderDate = LocalDateTime.now()
            return order
        }
    }
}