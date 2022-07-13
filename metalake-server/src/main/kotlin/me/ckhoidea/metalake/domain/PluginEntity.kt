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
    var version: String,
    var jarPath: String,
    var nameClass: String,
    // pluginUID通过jar路径，version与nameClass生成
    @Column(unique = true)
    var pluginUID: String
)