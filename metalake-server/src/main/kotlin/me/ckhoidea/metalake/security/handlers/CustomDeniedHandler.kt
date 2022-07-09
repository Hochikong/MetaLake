package me.ckhoidea.metalake.security.handlers

import me.ckhoidea.metalake.utils.JSONMapper
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomNotAuthentication : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.characterEncoding = "utf-8";
        response.contentType = "application/json;charset=utf-8";
        response.writer.print(JSONMapper.writeValueAsString(object {
            val status = 401
        }));
    }
}