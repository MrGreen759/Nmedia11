package ru.netology.nmedia11

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia11.repository.PostRepoNet
import ru.netology.nmedia11.utils.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

@RequiresApi(Build.VERSION_CODES.O)
class PostViewModel(application: Application): AndroidViewModel(application) {
    private val emptyPost = Post (
        id = -1L,
        author = "",
        content = "",
        published = 0L,
        likes = 0,
        shares = 0,
        views = 0,
        video = ""
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private val repository: PostRepoNet = PostRepoNet()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(emptyPost)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAsync(object : PostRepository.GetAllCalback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun likeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            val current = _data.value?.posts.orEmpty()
            var needLike = false

                for (i in 0..current.size - 1) {
                    if (current[i].id == id) {
                        if(current[i].likedByMe) {
                            --current[i].likes
                        } else {
                            ++current[i].likes
                            needLike = true
                        }
                        current[i].likedByMe = !current[i].likedByMe
                        break
                    }
                }

             _data.postValue(_data.value?.copy(posts = current))

            try {
                if (needLike) repository.likeById(id)
                else repository.unLikeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun share(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            val current = _data.value?.posts.orEmpty()

            for (i in 0..current.size - 1) {
                if (current[i].id == id) {
                    ++current[i].shares
                    break
                }
            }

            _data.postValue(_data.value?.copy(posts = current))

            try {
                repository.share(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun remove(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().filter { it.id != id }))
            try {
                repository.remove(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
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
