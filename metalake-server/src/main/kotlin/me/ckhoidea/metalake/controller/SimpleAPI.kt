package me.ckhoidea.metalake.controller


import me.ckhoidea.metalake.repository.AuthRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
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
}