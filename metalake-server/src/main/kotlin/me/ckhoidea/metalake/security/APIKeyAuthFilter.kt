package me.ckhoidea.metalake.security

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest

/**
 * 引用自：https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework
 *        /security/web/authentication/preauth/AbstractPreAuthenticatedProcessingFilter.html
 * 用于处理处理预身份验证请求的过滤器的基类，其中假定主体已通过外部系统的身份验证。
 * 其目的只是从传入请求中提取有关主体的必要信息，而不是对它们进行身份验证。
 * 外部身份验证系统可以通过请求数据（例如，预身份验证系统可以提取的标头或 cookie）提供此信息。
 * 假设外部系统负责数据的准确性并防止提交伪造值。
 * 子类必须实现getPreAuthenticatedPrincipal()and getPreAuthenticatedCredentials()方法。
 *
 * 提取header中的指定字段
 * */
class APIKeyAuthFilter(private val accessKey: String, private val accessSecret: String, private val hash: String) :
    AbstractPreAuthenticatedProcessingFilter() {
    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): Map<String, String> {
        return mapOf(
            "Key" to request.getHeader(accessKey),
            "Secret" to request.getHeader(accessSecret),
            "Hash" to request.getHeader(hash)
        )
    }

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest): String {
        return ""
    }
}