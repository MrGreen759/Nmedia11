package ru.netology.nmedia11

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

class PostViewModel: ViewModel() {
    private val repository: PostRepo = PostRepo()
    val data = repository.get()
    fun likeById(id: Long) = repository.likeById(id)
    fun share(id: Long) = repository.share(id)
}
