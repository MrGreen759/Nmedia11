package ru.netology.nmedia11

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Int,
    var shares: Int,
    val views: Int,
    val video: String
)

private val emptyPost = Post (
    id = -1L,
    author = "",
    content = "",
    published = "",
    likes = 0,
    shares = 0,
    views = 0,
    video = ""
    )

class PostViewModel(application: Application): AndroidViewModel(application) {
    @RequiresApi(Build.VERSION_CODES.O)
    private val repository: PostRepoRoom = PostRepoRoom(AppDb.getInstance(application).postDao())
    @RequiresApi(Build.VERSION_CODES.O)
    val data = repository.get()
    val edited = MutableLiveData(emptyPost)

    @RequiresApi(Build.VERSION_CODES.O)
    fun likeById(id: Long) = repository.likeById(id)
    @RequiresApi(Build.VERSION_CODES.O)
    fun share(id: Long) = repository.share(id)
    @RequiresApi(Build.VERSION_CODES.O)
    fun remove(id: Long) = repository.remove(id)

    @RequiresApi(Build.VERSION_CODES.O)
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = emptyPost
    }

    fun addAndEdit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

}
