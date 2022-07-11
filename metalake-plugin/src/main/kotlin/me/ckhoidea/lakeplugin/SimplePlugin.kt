package me.ckhoidea.lakeplugin

import me.ckhoidea.metalake.share.LakePluginBase

class SimplePlugin : LakePluginBase {
    override fun translateRequests(req: String?): String {
        if (req is String) {
            return req + "TRANS"
        }
        return ""
    }

    fun plus(X: Int, Y: Int): Int {
        return X + Y
    }
}