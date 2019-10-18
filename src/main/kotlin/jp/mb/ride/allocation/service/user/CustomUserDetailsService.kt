package jp.mb.ride.allocation.service.user

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class CustomUserDetailsService(
        val repository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val (userName, password, roles) = repository.findById(username!!)
                .orElseThrow { throw EntityNotFoundException("$username not found") }

        return User
                .withUsername(username)
                .password(password)
                .authorities(roles)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build()
    }
}
