package org.archguard.scanner.analyser.api.parser

import io.kotest.matchers.shouldBe
import org.archguard.scanner.analyser.api.postman.PostmanReader
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class PostmanParserTest {
    @Test
    fun should_print_out() {
        val file = javaClass.getResource("/openapi/CircleCI.postman_collection.json")!!
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(File(file.toURI()).absolutePath)
        val postmanParser = PostmanParser()
        val listList = postmanParser.parse(collection)!!

        listList.size shouldBe 17
        listList[0].items.size shouldBe 2
    }

    @Test
    fun should_print_out_2() {
        val boxJson = javaClass.getResource("/openapi/Box.json")!!
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(File(boxJson.toURI()).absolutePath)
        val postmanParser = PostmanParser()

        val listList = postmanParser.parse(collection)!!

        listList.size shouldBe 1
        listList[0].items.size shouldBe 5
    }

    @Test
    fun should_hande_description() {
        val boxJson = javaClass.getResource("/postman/Wechat-Simple.postman_collection.json")!!
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(File(boxJson.toURI()).absolutePath)
        val postmanParser = PostmanParser()

        val listList = postmanParser.parse(collection)!!

        listList.size shouldBe 1
        listList[0].items.size shouldBe 10


        val firstText = listList[0].items[0].renderDisplayText()
        firstText shouldBe  """### name: 查询投诉单列表
GET /v3/merchant-service/complaints-v2?limit=5&offset=10&begin_date=2022-11-01&end_date=2022-11-11&complainted_mchid={}?limit=5&offset=10&begin_date=2022-11-01&end_date=2022-11-11&complainted_mchid={{mchid}}
Response Body: 200: {}"""
    }
}
