package me.ckhoidea.lakeplugin

import me.ckhoidea.lakeplugin.Utils.exceptionToString
import me.ckhoidea.metalake.share.LakePluginInterface


class LibraryPlugin : LakePluginInterface {
    override fun translateRequests(req: Map<String, Any>): String {
        try {
            return when (req["query"] as String) {
                "conditions" -> {
                    ""
                }
                "preview" -> {
                    "SELECT preview, secondary_preview FROM djs_books WHERE gallery_id = ${req["galleryID"] as String}"
                }
                "associateMeta" -> {
                    "SELECT * FROM djs_associate WHERE gallery_id = ${req["galleryID"] as String}"
                }
                "subQuery" -> {
                    ""
                }
                "SQL" -> {
                    req["statement"] as String
                }
                else -> {
                    "SELECT * FROM djs_books limit 1;"
                }
            }
        } catch (e: Exception) {
            val (x, y) = exceptionToString()
            e.printStackTrace(y)
            return x.buffer.toString()
        }
    }

    override fun castResponse(response: Any?, param: MutableMap<String, Any>?): Any {
//        val result = response!! as List<Map<String, *>>
//        for (book in result) {
//            val bis = ByteArrayInputStream(book["preview"] as ByteArray?)
//            val bImage2: BufferedImage = ImageIO.read(bis)
//            ImageIO.write(bImage2, "png", File("output.png"))
//
//        }
        return response ?: "NO_DATA"
    }
}