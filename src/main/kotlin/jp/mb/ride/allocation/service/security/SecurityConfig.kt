package jp.mb.ride.allocation.service.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AndRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(val tokenManager: JwtTokenManager) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()

        http.sessionManagement().sessionCreationPolicy(STATELESS)

        http.authorizeRequests()
                .antMatchers("/users/sign-in").permitAll()
                .antMatchers("/users/sign-up").permitAll()

                .antMatchers("/operators/**").hasRole("OPERATOR")
                .antMatchers("/drivers/**").hasRole("DRIVER")
                .antMatchers("/passengers/**").hasRole("PASSENGER")
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(JwtTokenFilter(tokenManager, NegatedRequestMatcher(AndRequestMatcher(
                        AntPathRequestMatcher("/users/**")
                ))),
                        UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/**",
                "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/public",
                "/csrf", "/", "/error")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean("authenticationManager")
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}
