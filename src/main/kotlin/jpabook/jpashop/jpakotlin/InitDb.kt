package jpabook.jpashop.jpakotlin

import jpabook.jpashop.jpakotlin.domain.*
import jpabook.jpashop.jpakotlin.domain.Item.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Component
class InitDb {
    @Autowired
    private lateinit var initService: InitService

    @PostConstruct
    fun init() {
        initService.dbInit1()
        initService.dbInit2()
    }

    @Component
    @Transactional
    internal class InitService {
        @Autowired
        private lateinit var em: EntityManager
        fun dbInit1() {
            val member: Member = createMember("userA", "서울", "1", "1111")
            em.persist(member)
            val book1: Book = createBook("JPA1 BOOK", 10000, 100)
            em.persist(book1)
            val book2: Book = createBook("JPA2 BOOK", 20000, 100)
            em.persist(book2)
            val book3: Book = createBook("JPA3 BOOK", 30000, 100)
            em.persist(book3)
            val orderItem1: OrderItem = OrderItem.createOrderItem(book1, 10000, 1)
            val orderItem2: OrderItem = OrderItem.createOrderItem(book2, 10000, 2)
            val orderItem3: OrderItem = OrderItem.createOrderItem(book3, 30000, 2)
            val order: Order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2, orderItem3)
            em.persist(order)
        }

        fun dbInit2() {
            val member: Member = createMember("userB", "진주", "2", "2222")
            em.persist(member)
            val book1: Book = createBook("SPRING1 BOOK", 20000, 200)
            em.persist(book1)
            val book2: Book = createBook("SPRING2 BOOK", 40000, 300)
            em.persist(book2)
            val delivery: Delivery = createDelivery(member)
            val orderItem1: OrderItem = OrderItem.createOrderItem(book1, 20000, 3)
            val orderItem2: OrderItem = OrderItem.createOrderItem(book2, 40000, 4)
            val order: Order = Order.createOrder(member, delivery, orderItem1, orderItem2)
            em.persist(order)
        }

        private fun createMember(name: String, city: String, street: String, zipcode: String): Member {
            val member = Member()
            member.name = name
            member.address = Address(city, street, zipcode)
            return member
        }

        private fun createBook(name: String, price: Int, stockQuantity: Int): Book {
            val book = Book()
            book.name = name
            book.price = price
            book.stockQuantity = stockQuantity
            return book
        }

        private fun createDelivery(member: Member): Delivery {
            val delivery = Delivery()
            delivery.address = member.address
            return delivery
        }
    }
}
