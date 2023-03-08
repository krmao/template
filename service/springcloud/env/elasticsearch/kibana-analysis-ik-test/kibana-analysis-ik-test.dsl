//
// http://localhost:3309/app/dev_tools#/console
// https://blog.csdn.net/UbuntuTouch/article/details/100516428
//
POST /_analyze
{
  "text": "我爱北京天安门",
  "analyzer": "ik_smart"
}

POST /_analyze
{
  "text": "我爱北京天安门",
  "analyzer": "ik_max_word"
}

PUT chinese

PUT /chinese/_mapping
{
  "properties": {
    "content": {
      "type": "text",
      "analyzer": "ik_max_word",
      "search_analyzer": "ik_smart"
    }
  }
}

GET /chinese/_analyze
{
  "text": "我爱北京天安门",
  "analyzer": "ik_max_word"
}

PUT /chinese/_doc/1
{
  "content":"我爱北京天安门"
}

PUT  /chinese/_doc/2
{
  "content": "北京，你好"
}

GET /chinese/_search
{
  "query": {
    "match": {
      "content": "北京"
    }
  }
}

GET /chinese/_search
{
  "query": {
    "match": {
      "content": "天安门"
    }
  }
}

GET /chinese/_search
{
  "query": {
    "match": {
      "content": "北京天安门"
    }
  }
}
