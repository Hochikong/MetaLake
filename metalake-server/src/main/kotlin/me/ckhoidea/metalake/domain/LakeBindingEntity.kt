package me.ckhoidea.metalake.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "lake_bindings",
    indexes = [Index(name = "one_source_one_plugin", columnList = "dataSource, pluginPath")]
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
    var dataSourceDesc: String,
    @Column(unique = true)
    var dataSourceName: String,

    // jar plugin
    var pluginPath: String
)