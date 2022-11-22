package ru.netology.nmedia11

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Int,
    var shares: Int,
    val views: Int
)

private val emptyPost = Post (
    id = 0L,
    author = "",
    content = "",
    published = "",
    likes = 0,
    shares = 0,
    views = 0
    )

class PostViewModel: ViewModel() {
    private val repository: PostRepo = PostRepo()
    val data = repository.get()
    val edited = MutableLiveData(emptyPost)

    fun likeById(id: Long) = repository.likeById(id)
    fun share(id: Long) = repository.share(id)
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
