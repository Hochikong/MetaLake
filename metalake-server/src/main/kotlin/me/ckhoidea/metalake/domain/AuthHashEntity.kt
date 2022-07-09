package me.ckhoidea.metalake.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "auth_hash")
class AuthHashEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var createTime: Date,
    @Temporal(TemporalType.TIMESTAMP)
    var updateTime: Date,
    @Column(unique = true)
    var hash: String,
)