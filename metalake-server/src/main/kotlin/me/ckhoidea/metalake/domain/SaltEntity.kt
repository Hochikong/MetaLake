package me.ckhoidea.metalake.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "salts")
class SaltEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var createTime: Date,
    @Temporal(TemporalType.TIMESTAMP)
    var updateTime: Date,
    @Column(unique = true)
    var salt: String,
)