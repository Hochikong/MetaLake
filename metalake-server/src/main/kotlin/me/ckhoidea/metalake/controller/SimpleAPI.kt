package me.ckhoidea.metalake.controller


import me.ckhoidea.metalake.domain.dataclasses.QueryPost
import me.ckhoidea.metalake.repository.AuthRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SimpleAPI(
    @Autowired val authRepo: AuthRepository
) {
    @GetMapping("/api/test", produces = ["application/json"])
    fun test(): Map<String, Any> {
        return mapOf(
            "status" to 200
        )
    }

    @PostMapping("/api/test1", produces = ["application/json"])
    fun testPost(@RequestBody qp: QueryPost): Map<String, Any> {
        println(qp)
        return mapOf(
            "status" to 200
        )
    }
}