package org.archguard.scanner.analyser.api.parser

import org.archguard.model.*
import org.archguard.scanner.analyser.api.postman.*
import java.net.URI

sealed class ChildType {
    class NestedFolder(val folders: List<Folder>, val items: List<Item>) : ChildType()

    class Folder(val collection: ApiCollection) : ChildType()

    class Item(val items: List<ApiItem>) : ChildType()
}

class PostmanParser {
    private val variables: PostmanVariables = PostmanVariables(PostmanEnvironment())
    fun parse(collection: PostmanCollection): List<ApiCollection>? {
        return collection.item?.map {
            parseFolder(it, it.name)
        }?.flatten()
    }

    private fun parseFolder(item: PostmanFolder, folderName: String?): List<ApiCollection> {
        val details: MutableList<ApiCollection> = mutableListOf()
        if (item.item != null) {
            val childTypes = item.item.flatMap { parseChildItem(it, folderName, item.name) }

            details.addAll(childTypes.filterIsInstance<ChildType.Folder>()
                .map { it.collection })

            childTypes.filterIsInstance<ChildType.NestedFolder>().forEach {
                val folder = it.folders.map(ChildType.Folder::collection)
                details.addAll(folder)

                val items = it.items.map(ChildType.Item::items).flatten()
                details.add(ApiCollection(folderName ?: "", "", "", items))
            }

            val items = childTypes.filterIsInstance<ChildType.Item>().map { it.items }.flatten()
            if (items.isNotEmpty()) {
                val descriptionName = if (folderName == item.name) {
                    ""
                } else {
                    item.name ?: ""
                }

                details.add(ApiCollection(folderName ?: "", "", descriptionName, items))
            }
        } else if (item.request != null) {
            val apiItems = processApiItem(item as PostmanItem, folderName, item.name)?.let {
                listOf(it)
            } ?: listOf()

            val descriptionName = if (folderName == item.name) {
                ""
            } else {
                item.name ?: ""
            }

            details.add(ApiCollection(folderName ?: "", "", descriptionName, apiItems))
        }

        return details
    }

    private fun parseChildItem(subItem: PostmanItem, folderName: String?, itemName: String?): List<ChildType> {
        return when {
            subItem.item != null -> {
                val childTypes = subItem.item!!.map {
                    parseChildItem(it, folderName, itemName)
                }.flatten()

                val folder = childTypes.filterIsInstance<ChildType.Folder>()
                val items = childTypes.filterIsInstance<ChildType.Item>()

                if (folder.isNotEmpty() && items.isNotEmpty()) {
                    return listOf(ChildType.NestedFolder(folder, items))
                } else if (items.size == subItem.item!!.size) {
                    val collection =
                        ApiCollection(folderName ?: "", subItem.name ?: "", "", items.map { it.items }.flatten())
                    return listOf(ChildType.Folder(collection))
                }

                return childTypes
            }

            subItem.request != null -> {
                val apiItems = processApiItem(subItem, folderName, itemName)?.let {
                    listOf(it)
                } ?: listOf()

                listOf(ChildType.Item(apiItems))
            }

            else -> {
                listOf()
            }
        }
    }

    private fun processApiItem(
        subItem: PostmanItem,
        folderName: String?,
        itemName: String?,
    ): ApiItem? {
        val request = subItem.request
        val url: PostmanUrl? = request?.url
        val method = request?.method
        val body = request?.body
        val description = request?.description
        val name = subItem.name

        var uri = request?.getUrl(variables)
        uri = uri?.replace("http://UNDEFINED", "")
            ?.replace("https://UNDEFINED", "")
            ?.replace("UNDEFINED", "{}")

        try {
            val uriObj = URI(uri)
            uri = uriObj.path
        } catch (e: Exception) {
            // ignore
        }

        if (uri?.startsWith("/") == false) {
            val startIndex = uri.indexOf("/")
            if (startIndex > 0) {
                uri = uri.substring(startIndex)
            }
        }

        val responses = subItem.response?.map {
            Response(
                status = it.code ?: 0,
                parameters = listOf(),
                bodyMode = BodyMode.RAW_TEXT,
                bodyString = it.body ?: "",
            )
        }?.toList() ?: listOf()

        val req = Request(
            parameters = urlParameters(url),
            body = body?.formdata?.map { Parameter(it.key ?: "", it.value ?: "") } ?: listOf(),
        )

        if (uri?.isEmpty() == true) {
            return null
        }

        return ApiItem(
            method = method ?: "",
            path = uri ?: "",
            description = description.clean() ?: "",
            operationId = name ?: "",
            tags = listOf(folderName ?: "", itemName ?: ""),
            request = req,
            response = responses,
        )
    }

    private fun urlParameters(url: PostmanUrl?): List<Parameter> {
        val variable = url?.variable?.map {
            Parameter(it.key ?: "", formatValue(it.value))
        }

        val queries = url?.query?.map {
            Parameter(it.key ?: "", formatValue(it.value))
        }

        return (variable ?: listOf()) + (queries ?: listOf())
    }

    private fun formatValue(it: String?): String {
        val regex = Regex("^\\d+$")
        val boolRegex = Regex("^(true|false)$")

        return when {
            it?.matches(regex) == true -> it
            it?.matches(boolRegex) == true -> it
            else -> it ?: ""
        }
    }
}

private fun String?.clean(): String? {
    return this
        ?.replace("<[^>]*>".toRegex(), "")
        ?.replaceLineBreak()
        ?.replace("  ", " ")
}

private fun String?.replaceLineBreak(): String? {
    return this?.replace("\n", "")?.replace("\r", "")
}
