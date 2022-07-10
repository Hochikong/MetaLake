package me.ckhoidea.metalake.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "lake_bindings",
//    indexes = [Index(name = "one_source_one_plugin", columnList = "dataSource, pluginUID")]
)
class LakeBindingEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var createTime: Date,
    @Temporal(TemporalType.TIMESTAMP)
    var updateTime: Date,

    var accessKey: String,
    // data source url
    @Column(unique = true)
    var dataSource: String,
    var dataSourceDesc: String,
    @Column(unique = true)
    var dataSourceName: String,

    // jar plugin
    var pluginUID: String
)