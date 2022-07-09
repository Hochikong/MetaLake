package me.ckhoidea.metalake.domain


import java.util.*
import javax.persistence.*

@Entity
@Table(name = "authentications")
class AuthEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var createTime: Date,
    @Temporal(TemporalType.TIMESTAMP)
    var updateTime: Date,
    @Column(unique = true)
    var accessKey: String,
    @Column(unique = true)
    var accessSecret: String,
)