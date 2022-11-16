package ru.netology.nmedia11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun share(id: Long)
    fun edit(id: Long)
    fun save(post: Post)
    fun remove(id: Long)
}

class PostRepo: PostRepository {
    private var posts = listOf(
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 Мая в 18:36",
            likes = 799,
            shares = 184,
            views = 2898
        ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 2 → http://netolo.gy/fyb",
            published = "22 Мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 384,
            views = 1898
        ),
        Post(
            id = 3,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 3 → http://netolo.gy/fyb",
            published = "22 Мая в 19:36",
            likes = 2999,
            shares = 484,
            views = 898
        ),
        Post(
            id = 4,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 4 → http://netolo.gy/fyb",
            published = "24 Мая в 18:36",
            likes = 99,
            shares = 385,
            views = 3498
        ),
        Post(
            id = 5,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 5 → http://netolo.gy/fyb",
            published = "27 Мая в 17:36",
            likes = 1101,
            shares = 214,
            views = 4898
        ),
        Post(
            id = 6,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 6 → http://netolo.gy/fyb",
            published = "28 Мая в 20:36",
            likes = 1401,
            shares = 514,
            views = 2898
        )
    )
    private var nextId = posts.size.toLong()

    private val data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    // на кнопку "like"
    override fun likeById(id: Long) {
        posts = posts.map {
            if(it.id != id) it else it.copy(likedByMe = !it.likedByMe, likes = if (!it.likedByMe) it.likes+1 else it.likes-1)
        }
        data.value = posts
    }

    // на кнопку "share"
    override fun share(id: Long) {
        posts = posts.map {
            if(it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }

    override fun edit(id: Long) {
        TODO("Not yet implemented")
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            // TODO: remove hardcoded author & published
            posts = listOf(
                post.copy(
                    id = ++nextId,
                    author = "Me",
                    published = "now"
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    override fun remove(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

}
