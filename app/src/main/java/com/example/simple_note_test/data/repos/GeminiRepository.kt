package com.example.simple_note_test.data.repos

import android.util.Log
import com.example.simple_note_test.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class GeminiRepository @Inject constructor() {
    private val client = OkHttpClient()
    private val TAG = "GeminiRepository"

    suspend fun generateFromTitle(title: String): String = withContext(Dispatchers.IO) {
        // Prefer build config value (if set at build time), fall back to environment variable
        val buildKey = runCatching { BuildConfig.GEMINI_API_KEY }.getOrNull().orEmpty()
        val envKey = System.getenv("GEMINI_API_KEY") ?: "AIzaSyAJxSKPTHG5MSDFXbjpFj31RZsZboQE4yw"
        val apiKey = when {
            buildKey.isNotBlank() -> buildKey
            envKey.isNotBlank() -> envKey
            else -> ""
        }

        if (apiKey.isBlank()) {
            throw Exception("Gemini API key not configured. Set BuildConfig.GEMINI_API_KEY or GEMINI_API_KEY env var.")
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"
        val payload = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply { put(JSONObject().put("text", title)) })
                })
            })
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = payload.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-goog-api-key", apiKey)
            .post(body)
            .build()

        client.newCall(request).execute().use { resp ->
            val respBody = resp.body?.string() ?: throw Exception("Empty response from Gemini API")
            if (!resp.isSuccessful) {
                Log.e(TAG, "Gemini API error: ${resp.code} - $respBody")
                throw Exception("Gemini API error: ${resp.code}")
            }

            try {
                val root = JSONObject(respBody)

                // Helper: extract text from an object that may contain 'parts' array or a 'text' field
                fun extractFromContentObject(obj: JSONObject): String? {
                    if (obj.has("parts")) {
                        val parts = obj.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val sb = StringBuilder()
                            for (i in 0 until parts.length()) {
                                val p = parts.optJSONObject(i)
                                if (p != null && p.has("text")) sb.append(p.optString("text"))
                            }
                            if (sb.isNotEmpty()) return sb.toString().trim()
                        }
                    }
                    if (obj.has("text")) return obj.optString("text").trim()
                    return null
                }

                // 1) candidates -> content -> parts -> text (most common)
                if (root.has("candidates")) {
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCand = candidates.optJSONObject(0)
                        if (firstCand != null) {
                            if (firstCand.has("content")) {
                                val contentVal = firstCand.get("content")
                                when (contentVal) {
                                    is JSONArray -> {
                                        for (i in 0 until contentVal.length()) {
                                            val item = contentVal.optJSONObject(i) ?: continue
                                            val t = extractFromContentObject(item)
                                            if (!t.isNullOrBlank()) return@withContext t
                                        }
                                    }
                                    is JSONObject -> {
                                        val t = extractFromContentObject(contentVal)
                                        if (!t.isNullOrBlank()) return@withContext t
                                    }
                                }
                            }
                            if (firstCand.has("text")) return@withContext firstCand.optString("text").trim()
                            if (firstCand.has("output")) return@withContext firstCand.optString("output").trim()
                        }
                    }
                }

                // 2) outputs -> content -> parts -> text
                if (root.has("outputs")) {
                    val outputs = root.optJSONArray("outputs")
                    if (outputs != null && outputs.length() > 0) {
                        val firstOut = outputs.optJSONObject(0)
                        if (firstOut != null) {
                            if (firstOut.has("content")) {
                                val contentVal = firstOut.get("content")
                                when (contentVal) {
                                    is JSONArray -> {
                                        for (i in 0 until contentVal.length()) {
                                            val item = contentVal.optJSONObject(i) ?: continue
                                            val t = extractFromContentObject(item)
                                            if (!t.isNullOrBlank()) return@withContext t
                                        }
                                    }
                                    is JSONObject -> {
                                        val t = extractFromContentObject(contentVal)
                                        if (!t.isNullOrBlank()) return@withContext t
                                    }
                                }
                            }
                            if (firstOut.has("text")) return@withContext firstOut.optString("text").trim()
                        }
                    }
                }

                // 3) fallback: scan candidates for any text-like field
                if (root.has("candidates")) {
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null) {
                        for (i in 0 until candidates.length()) {
                            val cand = candidates.optJSONObject(i) ?: continue
                            // content nested search
                            if (cand.has("content")) {
                                val contentVal = cand.get("content")
                                when (contentVal) {
                                    is JSONObject -> {
                                        val t = extractFromContentObject(contentVal)
                                        if (!t.isNullOrBlank()) return@withContext t
                                    }
                                    is JSONArray -> {
                                        for (j in 0 until contentVal.length()) {
                                            val item = contentVal.optJSONObject(j) ?: continue
                                            val t = extractFromContentObject(item)
                                            if (!t.isNullOrBlank()) return@withContext t
                                        }
                                    }
                                }
                            }
                            // direct fields
                            if (cand.has("text")) return@withContext cand.optString("text").trim()
                            if (cand.has("output")) return@withContext cand.optString("output").trim()
                        }
                    }
                }

                // final fallback: return the raw response trimmed
                return@withContext respBody.trim()
            } catch (e: Exception) {
                Log.w(TAG, "Failed to parse Gemini response, returning raw body", e)
                return@withContext respBody.trim()
            }
        }
    }
}
