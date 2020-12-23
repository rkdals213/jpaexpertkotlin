package jpabook.jpashop.jpakotlin.repository.query

import com.fasterxml.jackson.annotation.JsonIgnore

class OrderItemQueryDto(//주문번호
    @field:JsonIgnore var id: Long, //상품 명
    var itemName: String, //주문 가격
    var orderPrice: Int, //주문 수량
    var count: Int
)