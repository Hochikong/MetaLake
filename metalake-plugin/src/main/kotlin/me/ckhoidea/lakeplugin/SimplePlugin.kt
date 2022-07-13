package me.ckhoidea.lakeplugin

import me.ckhoidea.metalake.share.LakePluginInterface
import kotlin.reflect.typeOf

class SimplePlugin : LakePluginInterface {
    override fun translateRequests(req: MutableMap<String, Any>): String {
        try {
            val associatePropertyAndValue: Map<String, String> =
                (req["djs_associate"] ?: mapOf<String, String>()) as Map<String, String>
            val conditions = associatePropertyAndValue.map { Pair(it.key, it.value) }.toList()

            if (conditions.isNotEmpty()) {
                var conditionSTMT = "WHERE "
                for (i in conditions.indices) {
                    conditionSTMT += if (i == 0) {
                        "(property = '${conditions[i].first}' AND p_value LIKE '%${conditions[i].second}%') "
//                    } else if (i == (conditions.size - 1)) {
//                        "AND (property = '${conditions[i].first}' AND p_value LIKE '%${conditions[i].second}%') "
                    } else {
                        "AND (property = '${conditions[i].first}' AND p_value LIKE '%${conditions[i].second}%') "
                    }
                }

                return "SELECT * FROM djs_books WHERE gallery_id IN (SELECT gallery_id FROM djs_associate $conditionSTMT);"
            }
            return "SELECT * FROM djs_books LIMIT 5;"
        } catch (e: Exception) {
            e.printStackTrace()
            return "ERROR"
        }
    }

    override fun castResponse(response: Any?): Map<String, Any> {
        val result = response!! as List<Map<String, *>>
        for (book in result) {
            book.map { println(it.value!!::class.java.typeName) }
            book.map { println(it.value) }
        }
        return mapOf("R" to response!!)
    }
}