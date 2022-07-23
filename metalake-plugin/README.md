# 简单插件的使用说明

## 支持的查询:

## 1.复杂查询：

查询体：

```json
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
```

对应的SQL为：

```sql
select *
from djs_books
where (username like '%jack%')
   or (uid = '123455' or title like '%THIS IS TITLE%')
```

## 2.根据associate表的子条件查询：

查询体：

```json
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
```

对应的SQL为：

```sql
select *
from djs_books
where gallery_id IN (select distinct gallery_id
                     from djs_associate
                     WHERE (property = 'Artists' AND p_value in ('A', 'B', 'C'))
                        OR (property = 'Artists' AND p_value = 'Somebody')
                        OR (property = 'Parodies' AND p_value = 'GAL'))
```

## 3.查询缩略图：

请求体：

```json
            {
  "lakeName": "Doujinshi",
  "queryBody": {
    "query": "preview",
    "galleryID": 12324
  }
}
```

对应的SQL为：

```sql
select preview, secondary_preview
from djs_books
where gallery_id = 12324
```

## 4.查询元数据属性：

请求体：

```json
            {
  "lakeName": "Doujinshi",
  "queryBody": {
    "query": "associateMeta",
    "galleryID": 12324
  }
}
```

对应的SQL为：

```sql
select *
from djs_associate
where gallery_id = 12324
```

## 5.自定义SQL查询（仅支持查询）：

请求体：

```json
            {
  "lakeName": "Doujinshi",
  "queryBody": {
    "query": "SQL",
    "statement": "SELECT * FROM xxxx"
  }
}
```


