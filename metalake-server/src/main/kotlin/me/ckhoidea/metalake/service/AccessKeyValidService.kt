package me.ckhoidea.metalake.service

import me.ckhoidea.metalake.repository.AuthRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccessKeyValidService(
    @Autowired val authRepo: AuthRepository
) {
    fun isValidKey(key: String): Boolean {
        val allKeys = authRepo.findAll().map { it.accessKey }.toList()
        return key in allKeys
    }
}