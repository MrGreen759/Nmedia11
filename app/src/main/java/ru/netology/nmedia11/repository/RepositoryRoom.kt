//package ru.netology.nmedia11
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.lifecycle.Transformations
//
//@RequiresApi(Build.VERSION_CODES.O)
//class PostRepoRoom(
//    private val dao: PostDao
//) : PostRepository {
//    private var posts = emptyList<Post>()

    //      всё, что закомментировано ниже - только для создания БД при первом запуске
//    private var posts = listOf(
//        Post(
//            id = 0,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            published = "21 Мая в 18:36",
//            likes = 799,
//            shares = 184,
//            views = 2898,
//            video = "https://www.youtube.com/watch?v=KYByIYwhFn4"
//        ),
//        Post(
//            id = 0,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Это пост № 2 → http://netolo.gy/fyb",
//            published = "22 Мая в 18:36",
//            likedByMe = false,
//            likes = 999,
//            shares = 384,
//            views = 1898,
//            video = ""
//        ),
//        Post(
//            id = 0,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Это пост № 3 → http://netolo.gy/fyb",
//            published = "22 Мая в 19:36",
//            likes = 2999,
//            shares = 484,
//            views = 898,
//            video = "https://www.youtube.com/watch?v=cH_4oIxPBnQ"
//        ),
//        Post(
//            id = 0,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Это пост № 4 → http://netolo.gy/fyb",
//            published = "24 Мая в 18:36",
//            likes = 99,
//            shares = 385,
//            views = 3498,
//            video = ""
//        ),
//        Post(
//            id = 0,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Это пост № 5 → http://netolo.gy/fyb",
//            published = "27 Мая в 17:36",
//            likes = 1101,
//            shares = 214,
//            views = 4898,
//            video = ""
//        ),
//        Post(
//            id = 0,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Это пост № 6 → http://netolo.gy/fyb",
//            published = "28 Мая в 20:36",
//            likes = 1401,
//            shares = 514,
//            views = 2898,
//            video = "https://www.youtube.com/watch?v=Rjcvfv8bqFo"
//        )
//    )
//    private val data = MutableLiveData(posts)
//
//    init {
//        save(posts[0])
//        save(posts[1])
//        save(posts[2])
//        save(posts[3])
//        save(posts[4])
//        save(posts[5])
//    }
//
//    override fun get() = Transformations.map(dao.getAll()) { list ->
//        list.map {
//            it.toDto()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun save(post: Post) {
//        dao.save(PostEntity.fromDto(post))
//    }
//
//    override fun likeById(id: Long) {
//        dao.likeById(id)
//    }
//
//    override fun share(id: Long) {
//        dao.share(id)
//    }
//
//    override fun remove(id: Long) {
//        dao.remove(id)
//     }
//
//}
