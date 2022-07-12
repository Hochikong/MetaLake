package me.ckhoidea.metalake.service

import me.ckhoidea.metalake.repository.PluginRepository
import me.ckhoidea.metalake.share.LakePluginBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URL
import java.net.URLClassLoader

@Service
class PluginCentreService(
    @Autowired val pluginRepo: PluginRepository
) {
    val pluginInstances = mutableListOf<Pair<String, Any>>()

    fun initAllPlugins() {
        val allPluginPairs = pluginRepo.findAll().map { Pair(it.nameClass, it.jarPath) }.toList()
        for (cfg in allPluginPairs) {
            try {
                val result = this.loadPlugin(cfg.first, cfg.second)
                pluginInstances.add(result)
            } catch (e: Exception) {
                println("Loading ${cfg.first} Error: ${e.printStackTrace()}")
            }
        }
    }

    fun getInstance(nameClass: String): Any? {
        for (I in this.pluginInstances) {
            if (I.first == nameClass) {
                return I.second
            }
        }
        return null
    }

    private fun loadPlugin(nameClass: String, jarPath: String): Pair<String, Class<*>> {
        val jarPathForLoading = "file:$jarPath"

        val urlClassLoader = URLClassLoader(arrayOf(URL(jarPathForLoading)))
        val driver = urlClassLoader.loadClass(nameClass)
        return Pair(nameClass, driver)
    }
}