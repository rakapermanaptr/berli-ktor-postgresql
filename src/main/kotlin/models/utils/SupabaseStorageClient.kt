package com.creospace.models.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64

object SupabaseStorageClient {
    private val supabaseUrl = System.getenv("SUPABASE_URL")?.trimEnd('/')
        ?: throw IllegalStateException("Missing SUPABASE_URL environment variable")
    private val serviceRoleKey = System.getenv("SUPABASE_SERVICE_ROLE_KEY")
        ?: throw IllegalStateException("Missing SUPABASE_SERVICE_ROLE_KEY environment variable")
    private val bucket = System.getenv("SUPABASE_BUCKET")
        ?: throw IllegalStateException("Missing SUPABASE_BUCKET environment variable")


    private val http = HttpClient(CIO)


    suspend fun uploadBytes(objectPath: String, bytes: ByteArray, contentType: String = "application/octet-stream"): String {
        val url = "$supabaseUrl/storage/v1/object/$bucket/$objectPath"
        val resp: HttpResponse = http.post(url) {
            header("Authorization", "Bearer $serviceRoleKey")
            header("Content-Type", contentType)
            setBody(bytes)
        }


        val text = resp.bodyAsText()
        if (!resp.status.isSuccess()) {
            throw RuntimeException("Supabase upload failed: ${'$'}{resp.status}. Body: ${'$'}text")
        }


        // For a public bucket the canonical public URL is:
        return "$supabaseUrl/storage/v1/object/public/$bucket/$objectPath"
    }


    suspend fun uploadBase64(objectPath: String, base64: String): String {
        val cleaned = base64.substringAfter("base64,")
        val bytes = Base64.getDecoder().decode(cleaned)
        val ct = detectImageContentType(bytes) ?: "application/octet-stream"
        return uploadBytes(objectPath, bytes, ct)
    }


    // create signed url (for private buckets)
    @Serializable
    private data class SignRequest(val expiresIn: Int)


    @Serializable
    private data class SignResponse(val signedURL: String? = null, val error: String? = null)


    suspend fun createSignedUrl(objectPath: String, expiresInSeconds: Int = 60): String {
        val url = "$supabaseUrl/storage/v1/object/sign/$bucket/$objectPath"
        val resp = http.post(url) {
            header("Authorization", "Bearer $serviceRoleKey")
            header("Content-Type", "application/json")
            setBody(Json.encodeToString(SignRequest.serializer(), SignRequest(expiresInSeconds)))
        }
        val txt = resp.bodyAsText()
        if (!resp.status.isSuccess()) throw RuntimeException("Sign url failed: ${'$'}{resp.status}. Body: ${'$'}txt")
        val json = Json.parseToJsonElement(txt).jsonObject
        return json["signedURL"]?.jsonPrimitive?.content
            ?: throw RuntimeException("No signedURL in response: ${'$'}txt")
    }


    fun detectImageContentType(bytes: ByteArray): String? {
        if (bytes.size >= 4) {
            if (bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte()) return "image/jpeg"
            if (bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte()) return "image/png"
            if (bytes[0] == 0x47.toByte() && bytes[1] == 0x49.toByte()) return "image/gif"
        }
        return null
    }
}