package me.ckhoidea.metalake.common

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit

object ConnectionsCache {
    var cacheManager: Cache<String, Any> = Caffeine.newBuilder()
        // 设置最后一次写入或访问后经过固定时间过期
        .expireAfterAccess(30, TimeUnit.MINUTES)
        // 初始的缓存空间大小
        .initialCapacity(10)
        // 缓存的最大条数 Eviction
        .maximumSize(100)
        .build()
}