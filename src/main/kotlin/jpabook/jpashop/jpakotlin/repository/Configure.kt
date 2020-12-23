package jpabook.jpashop.jpakotlin.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QuerydslConfig(

    @Qualifier(value = "entityManagerFactory")
    val em: EntityManager,

) {
    @Bean
    fun JpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}