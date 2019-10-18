package jp.mb.ride.allocation.service.user

import javax.persistence.*

@Entity
@Table
data class User(
        @Id
        val username: String,

        @Column(nullable = false)
        val password: String,

        @ElementCollection(fetch = FetchType.EAGER)
        var roles: MutableList<Role>
)
