package com.cegeka.cnads

import io.quarkus.runtime.StartupEvent
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject
import javax.persistence.*
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
class ExampleResource(@Inject var userRepo:UserRepository) {
    @GET
    @Path("greet/{username}")
    @Transactional
    fun hello(@PathParam("username") username: String):Greeting {
        userRepo.save(User(0, username))

        return Greeting("Hello ${username}")
    }

    @Transactional
    @GET
    @Path("greets")
    fun find():Set<String> {
        return userRepo.findAll().map { it.name }.toSet()
    }
}

data class Greeting(val message:String)

@Entity
@Table(name = "users")
@NamedQuery(
        name = "User.findAll",
        query = "SELECT u FROM User u ORDER BY u.name"
)
data class User(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long, var name: String)

@ApplicationScoped
class UserRepository(@Inject var em: EntityManager) {
    fun save(user: User) {
        em.persist(user)
    }

    fun findAll(): List<User> {
        return em.createNamedQuery("User.findAll", User::class.java).resultList
    }
}