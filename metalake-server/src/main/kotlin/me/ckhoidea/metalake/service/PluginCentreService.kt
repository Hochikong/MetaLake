package me.ckhoidea.metalake.service

import me.ckhoidea.metalake.controller.SimpleAPI
import me.ckhoidea.metalake.repository.PluginRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URL
import java.net.URLClassLoader

@Service
class PluginCentreService(
    @Autowired val pluginRepo: PluginRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(PluginCentreService::class.java)
    val pluginInstances = mutableListOf<Pair<String, Any>>()

    fun initAllPlugins() {
        val allPluginPairs = pluginRepo.findAll().map { Pair(it.nameClass, it.jarPath) }.toList()
        for (cfg in allPluginPairs) {
            try {
                val result = this.loadPlugin(cfg.first, cfg.second)
                pluginInstances.add(result)
                logger.info("Init plugin: ${cfg.first}")
            } catch (e: Exception) {
                logger.error("Loading ${cfg.first} Error: \n")
                e.printStackTrace()
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