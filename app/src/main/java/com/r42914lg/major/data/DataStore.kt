package com.r42914lg.major.data

import android.content.Context
import com.r42914lg.major.model.Visitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

class ListFileStorage(
    private val context: Context,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
    }
) {
    private val file: File
        get() = File(context.filesDir, FILE_NAME)

    suspend fun write(items: List<Visitor>) = withContext(Dispatchers.IO) {
        val jsonString = json.encodeToString(items)
        file.writeText(jsonString)
    }

    suspend fun read(): List<Visitor> = withContext(Dispatchers.IO) {
        if (!file.exists()) {
            return@withContext emptyList()
        }
        runCatching {
            json.decodeFromString<List<Visitor>>(file.readText())
        }.getOrElse {
            emptyList()
        }
    }

    companion object {
        private const val FILE_NAME = "visitors.json"
    }
}