package me.ckhoidea.metalake.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "metalake_plugins", indexes = [Index(name = "one_path_multi_class", columnList = "jarPath, nameClass")])
class PluginEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var createTime: Date,
    @Temporal(TemporalType.TIMESTAMP)
    var updateTime: Date,
    var jarPath: String,
    var nameClass: String
)