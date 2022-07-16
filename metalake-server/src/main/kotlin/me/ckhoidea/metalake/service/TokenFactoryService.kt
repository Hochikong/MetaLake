package me.ckhoidea.metalake.service

import me.ckhoidea.metalake.domain.AuthEntity
import me.ckhoidea.metalake.repository.AuthRepository
import me.ckhoidea.metalake.utils.PasswordGenerator.PasswordGeneratorBuilder
import me.ckhoidea.metalake.utils.getCurrentDatetimeAsDate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class TokenFactoryService(
    @Autowired
    val authRepo: AuthRepository,
//    @Autowired
//    val saltRepo: SaltRepository,
//    @Autowired
//    val authHashRepo: AuthHashRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenFactoryService::class.java)
    fun generateNewAccessToken(): Map<String, String> {
        val passwordGenerator = PasswordGeneratorBuilder()
            .useDigits(true)
            .useLower(true)
            .useUpper(true)
            .build()
        val accessKey = passwordGenerator.generate(50)
        logger.info("Create new Access Key: ${accessKey.slice(0..5)}")
        val accessSecret = passwordGenerator.generate(70)

// 2022-07-14 停掉Hash的功能
//        var salts = saltRepo.findAll().map { it.salt }.toList()
//        if (salts.isEmpty()) {
//            for (i in 0..10) {
//                saltRepo.save(
//                    SaltEntity(
//                        createTime = getCurrentDatetimeAsDate(),
//                        updateTime = getCurrentDatetimeAsDate(),
//                        salt = passwordGenerator.generate(12)
//                    )
//                )
//            }
//            salts = saltRepo.findAll().map { it.salt }.toList()
//        }
//        val randomSalt: String = salts[Random().nextInt(salts.size)]
//        val hash = string2MD5(accessKey + accessSecret, randomSalt)

        authRepo.save(
            AuthEntity(
                createTime = getCurrentDatetimeAsDate(),
                updateTime = getCurrentDatetimeAsDate(),
                accessKey = accessKey,
                accessSecret = accessSecret
            )
        )

//        authHashRepo.save(
//            AuthHashEntity(
//                createTime = getCurrentDatetimeAsDate(),
//                updateTime = getCurrentDatetimeAsDate(),
//                hash = hash
//            )
//        )

        return mapOf("Key" to accessKey, "Secret" to accessSecret)
    }
}