package me.ckhoidea.metalake.config

import me.ckhoidea.metalake.repository.AuthHashRepository
import me.ckhoidea.metalake.repository.AuthRepository
import me.ckhoidea.metalake.security.APIKeyAuthFilter
import me.ckhoidea.metalake.security.handlers.CustomNotAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Order(1)
class APISecurityConfig(
    @Autowired
    val hashRepo: AuthHashRepository,
    @Autowired
    val authRepo: AuthRepository
) {
    private val requireKey: String = "X-ACCESS-KEY"
    private val requireSecret: String = "X-ACCESS-SECRET"
    private val requireHash: String = "X-ACCESS-HASH"

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val filter = APIKeyAuthFilter(requireKey, requireSecret, requireHash)

        filter.setAuthenticationManager(AuthenticationManager { authentication ->
            val headerMap = authentication.principal as Map<*, *>
            val key = headerMap["Key"] as String
            val secret = headerMap["Secret"] as String
            val hash = headerMap["Hash"] as String

            if (hash !in hashRepo.findAll().filter { !it.isMainHash }.map { it.hash }.toList()) {
                throw BadCredentialsException("No such hash")
            }

            if (key !in authRepo.findAll().map { Pair(it.accessKey, it.accessSecret) }.toList().map { it.first }
                    .toList()) {
                throw BadCredentialsException("No such key")
            } else {
                for (p in authRepo.findAll().map { Pair(it.accessKey, it.accessSecret) }.toList()) {
                    if (key == p.first) {
                        if (secret != p.second) {
                            throw BadCredentialsException("No such hash")
                        }
                    }
                }
            }

            authentication.isAuthenticated = true
            authentication
        })

        http.antMatcher("/api/**").csrf().disable().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(filter).authorizeRequests()
            .anyRequest().authenticated()
            // 实际上需要用401返回，但默认认证失败是返回403
            .and().exceptionHandling().authenticationEntryPoint(CustomNotAuthentication())

        return http.build()
    }
}