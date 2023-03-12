package ru.netology.nmedia11

import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia11.repository.PostRepoNet
import ru.netology.nmedia11.utils.SingleLiveEvent


@RequiresApi(Build.VERSION_CODES.O)
class PostViewModel(application: Application): AndroidViewModel(application) {
    private val emptyPost = Post (
        id = -1L,
        author = "",
        authorAvatar = "",
        content = "",
        published = 0L,
        likes = 0,
        shares = 0,
        views = 0,
        video = "",
        attachment = ""
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
    var errorOperation = MutableLiveData(0) // номер функции, в которой произошла ошибка
    var errorPostId = MutableLiveData(-1L)  // id поста, при обработке которого произошла ошибка

    init {
        loadPosts()
        errorOperation.value = 0
    }

    // загрузка списка постов
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

    // лайк / дизлайк
    fun likeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        var current = _data.value?.posts?.toList()!! // создаем копию данных
        var needLike = false
        errorOperation.value = 0

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

//        _data.postValue(_data.value?.copy(posts = current.filter { it.id != -100L }))
        _data.postValue(_data.value?.copy(posts = current))

            if (needLike) {

                // ставим лайк
                repository.likeAsync(id, object : PostRepository.ActionCallback {
                    override fun onSuccess(result: Int) {
                        val handler = Handler(Looper.getMainLooper())
                        if (result in 200..299) {
                            handler.post { Toast.makeText(getApplication(),"Успешно. Код: " + result, Toast.LENGTH_LONG).show() }
                        } else {
                            _data.postValue(_data.value?.copy(posts = old))
                            handler.post { Toast.makeText(getApplication(),"Неудачно. Код: " + result, Toast.LENGTH_LONG).show() }
                            errorOperation.postValue(1)
                            errorPostId.postValue(id)
                        }
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error = true))
                    }
                })
            } else {

                // снимаем лайк
                repository.unLikeAsync(id, object : PostRepository.ActionCallback {
                    override fun onSuccess(result: Int) {
                        val handler = Handler(Looper.getMainLooper())
                        if (result in 200..299) {
                            handler.post { Toast.makeText(getApplication(),"Успешно. Код: " + result, Toast.LENGTH_LONG).show() }
                        } else {
                            _data.postValue(_data.value?.copy(posts = old))
                            handler.post { Toast.makeText(getApplication(),"Неудачно. Код: " + result, Toast.LENGTH_LONG).show() }
                            errorOperation.postValue(1)
                            errorPostId.postValue(id)
                        }
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error = true))
                    }
                })
        }
    }

    // поделиться (репост)
    fun share(id: Long) {
        errorOperation.value = 0
        val old = _data.value?.posts.orEmpty()
        var current = _data.value?.posts?.toList()!! // создаем копию данных

        for (i in 0..current.size - 1) {
            if (current[i].id == id) {
                ++current[i].shares
                break
            }
        }

        _data.postValue(_data.value?.copy(posts = current))

        repository.shareAsync(id, object : PostRepository.ActionCallback {
            override fun onSuccess(result: Int) {
                val handler = Handler(Looper.getMainLooper())
                if (result in 200..299) {
                    handler.post { Toast.makeText(getApplication(), "Успешно. Код: " + result, Toast.LENGTH_LONG).show() }
                }
                else {
                    _data.postValue(_data.value?.copy(posts = old))
                    handler.post { Toast.makeText(getApplication(), "Неудачно. Код: " + result, Toast.LENGTH_LONG).show() }
                    errorOperation.postValue(2)
                    errorPostId.postValue(id)
                }
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    // удаление поста
    fun remove(id: Long) {
        errorOperation.value = 0
        val old = _data.value?.posts.orEmpty()
        _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().filter { it.id != id }))

        repository.removeAsync(id, object : PostRepository.ActionCallback {
            override fun onSuccess(result: Int) {
                val handler = Handler(Looper.getMainLooper())
                if (result in 200..299) {
                    handler.post { Toast.makeText(getApplication(), "Успешно. Код: " + result, Toast.LENGTH_LONG).show() }
                }
                else {
                    _data.postValue(_data.value?.copy(posts = old))
                    handler.post { Toast.makeText(getApplication(), "Неудачно. Код: " + result, Toast.LENGTH_LONG).show() }
                    errorOperation.postValue(3)
                    errorPostId.postValue(id)
                }
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    // сохранить
    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.ActionCallback {
                override fun onSuccess(result: Int) {
                    val handler = Handler(Looper.getMainLooper())
                    if (result in 200..299) {
                        handler.post { Toast.makeText(getApplication(), "Успешно. Код: " + result, Toast.LENGTH_LONG).show() }
                    }
                    else {
                        handler.post { Toast.makeText(getApplication(), "Неудачно. Код: " + result, Toast.LENGTH_LONG).show() }
                    }
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
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
