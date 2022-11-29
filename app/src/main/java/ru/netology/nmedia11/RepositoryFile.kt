package ru.netology.nmedia11

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class PostRepoFile(private val context: Context): PostRepository{
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 0L
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)
    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            // если файл есть - читаем
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, type)
            }
        } else {
            // если нет, записываем пустой пост с ID:1,
            // чтобы выводился не белый экран, а пустая карточка поста, можно редактировать
            posts = listOf(Post (
                id = 1L,
                author = "",
                content = "",
                published = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy в hh:mm")),
                likes = 0,
                shares = 0,
                views = 0,
                video = ""
            ))
            nextId = 1
            sync()
        }
        data.value = posts
    }

    override fun get(): LiveData<List<Post>> = data

    // на кнопку "like"
    override fun likeById(id: Long) {
        posts = posts.map {
            if(it.id != id) it else it.copy(likedByMe = !it.likedByMe, likes = if (!it.likedByMe) it.likes+1 else it.likes-1)
        }
        data.value = posts
        sync()
    }

    // на кнопку "share"
    override fun share(id: Long) {
        posts = posts.map {
            if(it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
        sync()
    }

    // сохранение поста
    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(post: Post) {
        if ((post.id == 0L) || (post.id == -1L)) {
            posts = listOf(
                post.copy(
                    id = ++nextId,
                    published = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy в hh:mm"))
                )
            ) + posts
            data.value = posts
            sync()
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        sync()
    }

    // удаление поста
    override fun remove(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    // сохранение постов в файл
    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

}