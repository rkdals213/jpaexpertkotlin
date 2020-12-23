package jpabook.jpashop.jpakotlin.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jpabook.jpashop.jpakotlin.domain.Item.Book
import jpabook.jpashop.jpakotlin.domain.Item.Item
import javax.persistence.*

@Entity
@Table(name = "order_item")
class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var item : Item? = null//주문 상품


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order : Order? = null//주문

    var orderPrice = 0//주문 가격

    var count = 0//주문 수량


    companion object {
        fun createOrderItem(book1: Book?, orderPrice: Int, count: Int): OrderItem {
            val orderItem = OrderItem()
            orderItem.item = book1
            orderItem.orderPrice = orderPrice
            orderItem.count = count
            return orderItem
        }
    }
}