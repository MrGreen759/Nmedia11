package ru.netology.nmedia11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepoSQLite(
    private val dao: PostDao
) : PostRepository {
    //private var posts = emptyList<Post>()
    private var posts = listOf(
        Post(
            id = 0,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 Мая в 18:36",
            likes = 799,
            shares = 184,
            views = 2898,
            video = "https://www.youtube.com/watch?v=KYByIYwhFn4"
        ),
        Post(
            id = 0,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 2 → http://netolo.gy/fyb",
            published = "22 Мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 384,
            views = 1898,
            video = ""
        ),
        Post(
            id = 0,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 3 → http://netolo.gy/fyb",
            published = "22 Мая в 19:36",
            likes = 2999,
            shares = 484,
            views = 898,
            video = "https://www.youtube.com/watch?v=cH_4oIxPBnQ"
        ),
        Post(
            id = 0,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 4 → http://netolo.gy/fyb",
            published = "24 Мая в 18:36",
            likes = 99,
            shares = 385,
            views = 3498,
            video = ""
        ),
        Post(
            id = 0,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 5 → http://netolo.gy/fyb",
            published = "27 Мая в 17:36",
            likes = 1101,
            shares = 214,
            views = 4898,
            video = ""
        ),
        Post(
            id = 0,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это пост № 6 → http://netolo.gy/fyb",
            published = "28 Мая в 20:36",
            likes = 1401,
            shares = 514,
            views = 2898,
            video = "https://www.youtube.com/watch?v=Rjcvfv8bqFo"
        )
    )
    private val data = MutableLiveData(posts)

    init {
        dao.save(posts[0])
        dao.save(posts[1])
        dao.save(posts[2])
        dao.save(posts[3])
        dao.save(posts[4])
        dao.save(posts[5])
        posts = dao.getAll()
        data.value = posts
    }

    override fun get(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun share(id: Long) {
        TODO("Not yet implemented")
    }

    override fun remove(id: Long) {
        dao.remove(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}
