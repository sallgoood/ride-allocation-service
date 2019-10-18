package jp.mb.ride.allocation.service.user

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ROLE_OPERATOR {
        override fun getAuthority(): String {
            return "ROLE_OPERATOR"
        }
    },
    ROLE_DRIVER {
        override fun getAuthority(): String {
            return "ROLE_DRIVER"
        }
    },
    ROLE_PASSENGER {
        override fun getAuthority(): String {
            return "ROLE_PASSENGER"
        }
    }
}
