package jpabook.jpashop.jpakotlin.domain.Item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("M")
class Movie : Item() {
    var director: String? = null
    var actor: String? = null
}