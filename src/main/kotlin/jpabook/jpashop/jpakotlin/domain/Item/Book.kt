package jpabook.jpashop.jpakotlin.domain.Item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("B")
class Book : Item() {
    var author: String? = null
    var isbn: String? = null
}