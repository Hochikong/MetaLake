package me.ckhoidea.lakeplugin

object Helper {
    fun queryHelp(): String {
        return """
            支持的查询:
            1. 复杂条件查询
            对应的SQL: 
            select * from djs_books where (username like '%jack%') or (uid = '123455' or title like '%THIS IS TITLE%')
            
            {
            	"lakeName": "Doujinshi",
            	"queryBody": {
            		"query": "conditions",
            		"match": [
            			{
            				"conj": "AND",
            				"rules": [
            					{
            						"conj": "AND",
            						"column": "username",
            						"how": "LIKE",
            						"type": "str",
            						"value": "jack"
            					}
            				]
            			},
            			{
            				"conj": "OR",
            				"rules": [
            					{
            						"conj": "AND",
            						"column": "uid",
            						"how": "MATCH",
            						"type": "str",
            						"value": "123455"
            					},
            					{
            						"conj": "OR",
            						"column": "title",
            						"how": "LIKE",
            						"type": "str",
            						"value": "THIS IS TITLE"
            					}
            				]
            			}
            		]
            	}
            }
            
            2. 查询缩略图
            对应的SQL: 
            select preview, secondary_preview from djs_books where gallery_id = 12324
            {
            	"lakeName": "Doujinshi",
            	"queryBody": {
            		"query": "preview",
            		"galleryID": 12324
            	}
            }
            
            3. 查询元数据关联属性（可能会有重复值）
            对应的SQL: 
            select * from djs_associate where gallery_id = 12324
            {
            	"lakeName": "Doujinshi",
            	"queryBody": {
            		"query": "associateMeta",
            		"galleryID": 12324
            	}
            }
            
            4. 复杂条件查询
            对应的SQL: 
            select * from djs_books where WHERE gallery_id IN (
                select distinct gallery_id from djs_associate WHERE (property = 'Artists' AND p_value in ('A', 'B', 'C')) 
                OR (property = 'Artists' AND p_value = 'Somebody') OR (property = 'Parodies' AND p_value = 'GAL')
            )
            
            {
            	"lakeName": "Doujinshi",
            	"queryBody": {
            		"query": "subQuery",
            		"match": [
            			{
            				"property": "Tags",
            				"how": "IN",
            				"type": "list",
            				"p_value": [
            					"A",
            					"B",
            					"C"
            				]
            			},
            			{
            				"property": "Artists",
            				"how": "MATCH",
            				"type": "str",
            				"p_value": "Somebody"
            			},
            			{
            				"property": "Parodies",
            				"how": "LIKE",
            				"type": "str",
            				"p_value": "GAL"
            			}
            		]
            	}
            }
            
            5. 自定义SQL查询，只允许select操作

            {
            	"lakeName": "Doujinshi",
            	"queryBody": {
            		"query": "SQL",
                    "statement": "SELECT * FROM xxxx"
            	}
            }
            
            
        """.trimIndent()
    }
}