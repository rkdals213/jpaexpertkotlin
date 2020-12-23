package jpabook.jpashop.jpakotlin.domain.Item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("A")
class Album : Item() {
    var artist: String? = null
    var etc: String? = null
}