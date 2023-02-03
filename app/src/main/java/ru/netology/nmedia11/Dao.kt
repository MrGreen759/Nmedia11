package ru.netology.nmedia11

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Insert
    fun insert(posts: List<PostEntity>)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    @RequiresApi(Build.VERSION_CODES.O)
    fun save(post: PostEntity) = if (post.id == 0L) {
//        post.published = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy в hh:mm"))
        insert(post)
    }
    else updateContentById(post.id, post.content)

    @Query("""
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    fun likeById(id: Long)

    @Query("""
        UPDATE PostEntity SET shares = shares + 1 WHERE id = :id
        """)
    fun share(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun remove(id: Long)

}
