package jpabook.jpashop.jpakotlin.api

import jpabook.jpashop.jpakotlin.domain.*
import jpabook.jpashop.jpakotlin.repository.OrderRepository
import jpabook.jpashop.jpakotlin.repository.OrderRepository2
import jpabook.jpashop.jpakotlin.repository.query.OrderQueryDto
import jpabook.jpashop.jpakotlin.repository.query.OrderQueryRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.stream.Collectors

@RestController
class OrderController(
    private var orderRepository: OrderRepository,
    private var orderRepository2: OrderRepository2,
    private var orderQueryRepository: OrderQueryRepository
) {
    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    fun ordersV1(): List<Order?>? {
        val all: List<Order?> = orderRepository.findAll()

        for (order in all) {
            order?.member?.name //Lazy 강제 초기화
            order?.delivery?.address //Lazy 강제 초기환
            val orderItems: List<OrderItem> = order!!.orderItems
            orderItems.forEach { o: OrderItem -> o.item?.name } //Lazy 강제 초기화
        }
        return all
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 트랜잭션 안에서 지연 로딩 필요
     */
    @GetMapping("/api/v2/orders")
    fun ordersV2(): List<OrderDto?>? {
        val orders = orderRepository.findAll()
        return orders.stream()
            .map { order -> OrderDto(order!!) }
            .collect(Collectors.toList())
    }

    class OrderDto(order: Order) {
        var orderId: Long = order.id!!
        var name: String = order.member?.name!!
        var orderDate: LocalDateTime = order.orderDate!! //주문시간
        var orderStatus: OrderStatus = order.status!!
        var address: Address = order.delivery?.address!!
        var orderItems: List<OrderItemDto> = order.orderItems.stream()
            .map { orderItem: OrderItem -> OrderItemDto(orderItem) }
            .collect(Collectors.toList())
    }

    class OrderItemDto(orderItem: OrderItem) {
        var itemName: String? = orderItem.item?.name //상품 명
        var orderPrice: Int = orderItem.orderPrice //주문 가격
        var count: Int = orderItem.count //주문 수량
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경가능)
     */
    @GetMapping("/api/v3/orders")
    fun ordersV3(): List<OrderDto?>? {
        val orders = orderRepository.findAllWithItem()
        return orders!!.stream()
            .map { order -> OrderDto(order!!) }
            .collect(Collectors.toList())
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("/api/v3.1/orders")
    fun ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") offset: Int, @RequestParam(value = "limit", defaultValue = "100") limit: Int
    ): List<OrderDto?>? {
        val orders: List<Order> = orderRepository2.findAllWithMemberDelivery(offset, limit)
        return orders.stream()
            .map{ order -> OrderDto(order!!) }
            .collect(Collectors.toList())
    }

    /**
     * V4. JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + N Query)
     * * - 페이징 가능
     */
    @GetMapping("/api/v4/orders")
    fun ordersV4(): List<OrderQueryDto?>? {
        return orderQueryRepository.findOrderQueryDtos()
    }

    /**
     * * V5. JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
     * * - 페이징 가능
     */
    @GetMapping("/api/v5/orders")
    fun ordersV5(): List<OrderQueryDto?>? {
        return orderQueryRepository.findAllByDto_optimization()
    }

    @GetMapping("/api/v1/querydsl-orders")
    fun querydsl_ordersV1(orderSearch: OrderSearch): MutableList<Order>? {
        return orderRepository2.findAll(orderSearch)
    }
}