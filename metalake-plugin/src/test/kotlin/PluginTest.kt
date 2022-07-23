import com.fasterxml.jackson.databind.ObjectMapper
import me.ckhoidea.lakeplugin.LibraryPlugin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import java.util.HashMap

class PluginTest {
    private fun loadJson(loc: String): HashMap<*, *>? {
        return ObjectMapper().readValue(
            Paths.get(loc).toFile(),
            HashMap::class.java
        )
    }

    @Test
    fun testEncryption() {
//        val docTest =
//            loadJson("C:\\Users\\ckhoi\\IdeaProjects\\MetaLake\\metalake-plugin\\src\\test\\resources\\cond2.json")!!["queryBody"]
//        val sqlTest = LibraryPlugin().translateRequests(docTest as HashMap<String, Any>)
//        println(sqlTest)

        val doc =
            loadJson("C:\\Users\\ckhoi\\IdeaProjects\\MetaLake\\metalake-plugin\\src\\test\\resources\\cond.json")!!["queryBody"]
        val sql = LibraryPlugin().translateRequests(doc as HashMap<String, Any>)
        assertEquals(
            "SELECT * FROM djs_books WHERE ( `nickname` LIKE '%SSS%' ) OR ( `uid` = 123455 OR `title` LIKE '%THIS IS TITLE%' )",
            sql.trim()
        )
        println(sql)

        val doc1 =
            loadJson("C:\\Users\\ckhoi\\IdeaProjects\\MetaLake\\metalake-plugin\\src\\test\\resources\\cond1.json")!!["queryBody"]
        val sql1 = LibraryPlugin().translateRequests(doc1 as HashMap<String, Any>)
        println(sql1)
        assertEquals(
            "SELECT * FROM djs_books WHERE gallery_id IN (SELECT DISTINCT gallery_id from djs_associate WHERE (property = 'Tags' AND p_value In (A, B, C)) OR (property = 'Artists' AND p_value = 'Somebody') OR (property = 'Parodies' AND p_value LIKE '%GAL%') )",
            sql1.trim()
        )
    }
}