package me.ckhoidea.lakeplugin

import me.ckhoidea.lakeplugin.Utils.exceptionToString
import me.ckhoidea.metalake.share.LakePluginInterface


class LibraryPlugin : LakePluginInterface {
    private val howMatch = mapOf("MATCH" to "=", "LIKE" to "LIKE", "IN" to "In")
    override fun translateRequests(req: Map<String, Any>): String {
        try {
            when (req["query"] as String) {
                "conditions" -> {
                    val head = "SELECT * FROM djs_books"
                    val optionalTail = "LIMIT 1"
                    var middle = ""
                    val matchRules: List<Map<String, Any>> = req["match"] as List<Map<String, Any>>
                    if (matchRules.isNotEmpty()) {
                        // 遍历match下所有内容
                        for (i in matchRules.indices) {
                            val singleRule = mutableListOf<String>()

                            val ruleGroup = matchRules[i]
                            if (i != 0) {
                                singleRule.add(ruleGroup["conj"].toString())
                            }

                            // 遍历ruleGroup
                            val rulesInGroup = ruleGroup["rules"] as List<Map<String, Any>>
                            singleRule.add("(")
                            for (ir in rulesInGroup.indices) {
                                val inGroupRule = rulesInGroup[ir]
                                var cond = ""

                                if (ir != 0) {
                                    cond =
                                        "${inGroupRule["conj"]} `${inGroupRule["column"]}` ${howMatch[inGroupRule["how"]]} "
                                    cond += if (inGroupRule["type"] == "str") {
                                        if (inGroupRule["how"] == "LIKE") {
                                            "'%${inGroupRule["value"]}%'"
                                        } else {
                                            "'${inGroupRule["value"]}'"
                                        }
                                    } else {
                                        "${inGroupRule["value"]}"
                                    }
                                } else {
                                    cond =
                                        "`${inGroupRule["column"]}` ${howMatch[inGroupRule["how"]]} "
                                    cond += if (inGroupRule["type"] == "str") {
                                        if (inGroupRule["how"] == "LIKE") {
                                            "'%${inGroupRule["value"]}%'"
                                        } else {
                                            "'${inGroupRule["value"]}'"
                                        }
                                    } else {
                                        "${inGroupRule["value"]}"
                                    }
                                }
                                singleRule.add(cond)
                            }
                            singleRule.add(") ")
                            middle += singleRule.joinToString(" ")
                        }
                        return "$head WHERE $middle"
                    } else {
                        return "$head $optionalTail"
                    }

                }
                "preview" -> {
                    return "SELECT preview, secondary_preview FROM djs_books WHERE gallery_id = ${req["galleryID"]}"
                }
                "associateMeta" -> {
                    return "SELECT * FROM djs_associate WHERE gallery_id = ${req["galleryID"]}"
                }
                "subQuery" -> {
                    val head = "SELECT * FROM djs_books"
                    val optionalTail = "LIMIT 1"
                    var middle = ""
                    val matchRules: List<Map<String, Any>> = req["match"] as List<Map<String, Any>>
                    if (matchRules.isNotEmpty()) {
                        middle += "WHERE gallery_id IN (SELECT DISTINCT gallery_id from djs_associate WHERE "
                        // 遍历match下所有内容
                        for (i in matchRules.indices) {
                            val singleSeq = mutableListOf<String>()

                            val ruleGroup = matchRules[i]
                            var cond = ""
                            if (i != 0) {
                                singleSeq.add("OR")
                            }

                            cond = "(property = '${ruleGroup["property"]}' AND p_value ${howMatch[ruleGroup["how"]]} "
                            cond += when (ruleGroup["type"]) {
                                "str" -> {
                                    if (ruleGroup["how"] == "LIKE") {
                                        "'%${ruleGroup["p_value"]}%'"
                                    } else {
                                        "'${ruleGroup["p_value"]}'"
                                    }
                                }
                                "list" -> {
                                    val ele = ruleGroup["p_value"] as List<String>
                                    "(${ele.joinToString(", ")})"
                                }
                                else -> "${ruleGroup["p_value"]}"
                            }
                            cond += ") "
                            singleSeq.add(cond)
                            middle += singleSeq.joinToString(" ")
                        }
                        return "$head $middle)"
                    } else {
                        return "$head $optionalTail"
                    }
                }
                "SQL" -> {
                    return req["statement"] as String
                }
                else -> {
                    return "SELECT * FROM djs_books limit 1;"
                }
            }
        } catch (e: Exception) {
            val (x, y) = exceptionToString()
            e.printStackTrace(y)
            return x.buffer.toString()
        }
    }

    override fun castResponse(response: Any?, param: MutableMap<String, Any>?): Any {
        val result = response!! as List<Map<String, *>>
        val responseList = mutableListOf<Map<String, *>>()
        for (map in result) {
            try {
                val new = map.toMutableMap()
                new.remove("id")

                if (param!!["queryType"] !in listOf("preview")) {
                    new.remove("preview")
                    new.remove("secondary_preview")
                }

                responseList.add(new)
            } catch (e: Exception) {

            }

        }
        return responseList
    }
}