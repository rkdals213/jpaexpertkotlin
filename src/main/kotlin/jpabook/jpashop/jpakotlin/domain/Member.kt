package jpabook.jpashop.jpakotlin.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.ArrayList
import javax.persistence.*

@Entity
class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id: Long? = null
    var name: String? = null

    @Embedded
    var address: Address? = null

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    var orders: MutableList<Order> = ArrayList<Order>()
}