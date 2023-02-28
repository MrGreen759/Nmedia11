package ru.netology.nmedia11.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.nmedia11.Post
import ru.netology.nmedia11.PostRepository
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepoNet: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:10999"
        private val jsonType = "application/json".toMediaType()
    }

    // получение списка постов
    override fun getAsync(callback: PostRepository.GetAllCalback) {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()?: throw RuntimeException("Body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

    // лайк
    override fun likeAsync(id: Long, callback: PostRepository.ActionCallback) {
        val request: Request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()
        callClient(request, callback)
    }

    // дизлайк
    override fun unLikeAsync(id: Long, callback: PostRepository.ActionCallback) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()
        callClient(request, callback)
    }

    // поделиться (репост)
    override fun shareAsync(id: Long, callback: PostRepository.ActionCallback) {
        val request: Request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("${BASE_URL}/api/slow/posts/$id/shares")
            .build()
        callClient(request, callback)
    }

    // удаление поста
    override fun removeAsync(id: Long, callback: PostRepository.ActionCallback) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        callClient(request, callback)
    }

    // сохранение поста
    override fun saveAsync(post: Post, callback: PostRepository.ActionCallback) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()
        callClient(request, callback)
    }

    private fun callClient(request: Request, callback: PostRepository.ActionCallback) {
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()?: throw RuntimeException("Body is null")
                    callback.onSuccess(response.code)
                }
            })
    }


}
