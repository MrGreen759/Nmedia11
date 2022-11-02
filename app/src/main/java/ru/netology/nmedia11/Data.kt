package ru.netology.nmedia11

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean,
    var likes: Int,
    var shares: Int,
    var views: Int
)
