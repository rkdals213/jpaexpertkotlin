package jpabook.jpashop.jpakotlin.domain

import javax.persistence.Embeddable

@Embeddable
class Address (
    var city: String,
    var street: String,
    var zipcode: String
)