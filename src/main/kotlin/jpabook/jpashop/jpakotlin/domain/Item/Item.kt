package jpabook.jpashop.jpakotlin.domain.Item

import jpabook.jpashop.jpakotlin.domain.Category
import java.util.ArrayList
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    var id: Long? = null
    var name: String? = null
    var price = 0
    var stockQuantity = 0

    @ManyToMany(mappedBy = "items")
    private val categories: MutableList<Category> = ArrayList<Category>()
}