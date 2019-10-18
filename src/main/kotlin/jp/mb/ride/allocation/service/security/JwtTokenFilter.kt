package jp.mb.ride.allocation.service.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtTokenFilter(
        private val jwtTokenProvider: JwtTokenManager,
        private val requiresAuthenticationRequestMatcher: RequestMatcher
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (requiresAuthenticationRequestMatcher.matches(request)) {
            try {
                val token = jwtTokenProvider.resolveToken(request)
                jwtTokenProvider.validateToken(token)
                SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(token)
            } catch (ex: Exception) {
                SecurityContextHolder.clearContext()
                response.sendError(401, ex.message)
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
