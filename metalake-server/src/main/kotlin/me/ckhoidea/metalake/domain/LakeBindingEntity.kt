package me.ckhoidea.metalake.domain

import java.util.*
import javax.persistence.*

/**
 * 一个lake只有一个唯一的access key，注册lake的时候自动生成
 * */
@Entity
@Table(
    name = "lake_bindings",
    indexes = [Index(name = "one_source_multi_plugin", columnList = "dataSource, pluginUID")]
)
class LakeBindingEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var createTime: Date,
    @Temporal(TemporalType.TIMESTAMP)
    var updateTime: Date,

    @Column(unique = true)
    var accessKey: String,
    // data source url
    var dataSource: String,
    var username: String = "",
    var password: String = "",
    var dataSourceDesc: String,
    @Column(unique = true)
    var dataSourceName: String,

    // jar plugin
    var pluginUID: String
)