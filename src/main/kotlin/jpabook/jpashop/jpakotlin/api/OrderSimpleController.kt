package jpabook.jpashop.jpakotlin.api

import jpabook.jpashop.jpakotlin.domain.Address
import jpabook.jpashop.jpakotlin.domain.Order
import jpabook.jpashop.jpakotlin.domain.OrderStatus
import jpabook.jpashop.jpakotlin.repository.OrderRepository
import jpabook.jpashop.jpakotlin.repository.simplequery.OrderSimpleQueryDto
import jpabook.jpashop.jpakotlin.repository.simplequery.OrderSimpleQueryRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.function.Function
import java.util.stream.Collectors

/**
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
class OrderSimpleApiController(
    private val orderRepository: OrderRepository,
    private val orderSimpleQueryRepository: OrderSimpleQueryRepository
) {


    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/simple-orders")
    fun ordersV1(): MutableList<Order?> {
        val all: MutableList<Order?> = orderRepository.findAll()
        for (order in all) {
            order?.member?.name //Lazy 강제 초기화
            order?.delivery?.address //Lazy 강제 초기화
        }
        return all
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    fun ordersV2(): MutableList<SimpleOrderDto> {
        val orders: MutableList<Order?> = orderRepository.findAll()
        return orders.stream()
            .map { order -> SimpleOrderDto(order!!) }
            .collect(Collectors.toList())
    }

    class SimpleOrderDto(order: Order) {
        var orderId: Long = order.id!!
        var name: String = order.member?.name!!
        var orderDate : LocalDateTime = order.orderDate!! //주문시간
        var orderStatus: OrderStatus = order.status!!
        var address: Address = order.delivery?.address!!
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - fetch join으로 쿼리 1번 호출
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     */
    @GetMapping("/api/v3/simple-orders")
    fun ordersV3(): List<SimpleOrderDto> {
        val orders: List<Order?>? = orderRepository.findAllWithMemberDelivery()
        return orders!!.stream()
            .map{ order -> SimpleOrderDto(order!!) }
            .collect(Collectors.toList())
    }

    /**
     * V4. JPA에서 DTO로 바로 조회
     * - 쿼리 1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     * V3는 추후 원하는데로 결과값을 가공해서 사용할 수 있지만 V4는 DTO 자체를 조회했기때문에 그러기 힘듦, 재사용성이 낮다
     * Repository가 api에 논리적으로 의존하는 형태가 됨, 논리적 계층이 깨짐
     */
    @GetMapping("/api/v4/simple-orders")
    fun ordersV4(): MutableList<OrderSimpleQueryDto?> {
        return orderSimpleQueryRepository.findOrderDtos()
    }
}