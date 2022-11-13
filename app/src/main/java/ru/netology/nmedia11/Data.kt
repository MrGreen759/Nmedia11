package ru.netology.nmedia11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int,
    val shares: Int,
    val views: Int
)

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
}

class PostRepo: PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 Мая в 18:36",
        likedByMe = false,
        likes = 999,
        shares = 184,
        views = 2898
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(likedByMe = !post.likedByMe, likes = if (!post.likedByMe) post.likes+1 else post.likes-1)
        data.value = post
    }

    override fun share() {
        post = post.copy(shares = post.shares+1)
        data.value = post
    }

}

class PostViewModel: ViewModel() {
    private val repository: PostRepo = PostRepo()
    val data = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
}
