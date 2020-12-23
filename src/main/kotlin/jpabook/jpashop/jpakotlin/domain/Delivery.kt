package jpabook.jpashop.jpakotlin.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    var id: Long? = null

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    var order: Order? = null

    @Embedded
    var address: Address? = null

    @Enumerated(EnumType.STRING)
    private val status: DeliveryStatus? = null //ENUM [READY(준비), COMP(배송)]
}
