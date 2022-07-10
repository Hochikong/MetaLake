package me.ckhoidea.metalake.service

import me.ckhoidea.metalake.repository.AuthHashRepository
import me.ckhoidea.metalake.utils.string2MD5
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PasswordValidService(
    @Autowired val authHashRepo: AuthHashRepository
) {
    fun isValidPassword(password: String): Boolean {
        val mainHash = authHashRepo.findAll().filter { it.isMainHash }.map { it.hash }.toList()
        return string2MD5("FryK2njKm5:^05", password) in mainHash
    }
}