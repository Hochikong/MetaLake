package me.ckhoidea.metalake.domain.dataclasses

data class QueryPost(val lakeName: String, val queryBody: Map<String, Any>)
