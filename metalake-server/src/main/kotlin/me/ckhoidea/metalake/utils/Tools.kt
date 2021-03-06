package me.ckhoidea.metalake.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.util.DigestUtils
import java.io.PrintWriter
import java.io.StringWriter
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun string2MD5(salt: String, input: String): String {
    return DigestUtils.md5DigestAsHex("$input / $salt".toByteArray())
}

fun string2instant(datetime: String): Instant {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val temporalAccessor = formatter.parse(datetime)
    val localDateTime = LocalDateTime.from(temporalAccessor)
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
    return Instant.from(zonedDateTime)
}

fun getCurrentDatetimeAsDate(): Date {
    return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
}

val JSONMapper: ObjectMapper = ObjectMapper().registerModule(
    KotlinModule.Builder()
        .withReflectionCacheSize(512)
        .configure(KotlinFeature.NullToEmptyCollection, false)
        .configure(KotlinFeature.NullToEmptyMap, false)
        .configure(KotlinFeature.NullIsSameAsDefault, false)
        .configure(KotlinFeature.SingletonSupport, false)
        .configure(KotlinFeature.StrictNullChecks, false)
        .build()
)

fun generateUIDByString(input: String): String {
    val md: MessageDigest = MessageDigest.getInstance("MD5")
    val messageDigest: ByteArray = md.digest(input.toByteArray())
    val no = BigInteger(1, messageDigest)
    var hashtext = no.toString(16)
    while (hashtext.length < 32) {
        hashtext = "0$hashtext"
    }
    return hashtext
}

/**
 * Usage：
 * }catch (e: Exception){
 *     if (enableDebug){
 *         val (sw, pw) = exceptionToString()
 *         e.printStackTrace(pw)
 *         return mapOf(
 *                    "code" to 500,
 *                    "error" to sw.buffer
 *                     )
 *
 * */
fun exceptionToString(): Pair<StringWriter, PrintWriter> {
    val sw = StringWriter();
    return Pair(sw, PrintWriter(sw))
}