package me.ckhoidea.metalake.service

import me.ckhoidea.metalake.domain.AuthEntity
import me.ckhoidea.metalake.domain.AuthHashEntity
import me.ckhoidea.metalake.domain.SaltEntity
import me.ckhoidea.metalake.repository.AuthHashRepository
import me.ckhoidea.metalake.repository.AuthRepository
import me.ckhoidea.metalake.repository.SaltRepository
import me.ckhoidea.metalake.utils.PasswordGenerator.PasswordGeneratorBuilder
import me.ckhoidea.metalake.utils.getCurrentDatetimeAsDate
import me.ckhoidea.metalake.utils.string2MD5
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class GenerateHashService(
    @Autowired
    val authRepo: AuthRepository,
    @Autowired
    val saltRepo: SaltRepository,
    @Autowired
    val authHashRepo: AuthHashRepository
) {
    fun generateNewHash() {
        val passwordGenerator = PasswordGeneratorBuilder()
            .useDigits(true)
            .useLower(true)
            .useUpper(true)
            .build()
        val accessKey = passwordGenerator.generate(40)
        val accessSecret = passwordGenerator.generate(60)

        var salts = saltRepo.findAll().map { it.salt }.toList()
        if (salts.isEmpty()) {
            for (i in 0..10) {
                saltRepo.save(
                    SaltEntity(
                        createTime = getCurrentDatetimeAsDate(),
                        updateTime = getCurrentDatetimeAsDate(),
                        salt = passwordGenerator.generate(12)
                    )
                )
            }
            salts = saltRepo.findAll().map { it.salt }.toList()
        }
        val randomSalt: String = salts[Random().nextInt(salts.size)]
        val hash = string2MD5(accessKey + accessSecret, randomSalt)

        authRepo.save(
            AuthEntity(
                createTime = getCurrentDatetimeAsDate(),
                updateTime = getCurrentDatetimeAsDate(),
                accessKey = accessKey,
                accessSecret = accessSecret
            )
        )

        authHashRepo.save(
            AuthHashEntity(
                createTime = getCurrentDatetimeAsDate(),
                updateTime = getCurrentDatetimeAsDate(),
                hash = hash
            )
        )

        println("You accessKey is $accessKey, secret is $accessSecret, hash is $hash")
    }
}