package ru.netology.nmedia11.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.netology.nmedia11.*
import ru.netology.nmedia11.databinding.FragmentOnePostBinding
import ru.netology.nmedia11.utils.IdArg
import ru.netology.nmedia11.utils.Utils

// Фрагмент просмотра карточки одного поста во весь экран

class OnePostFragment: Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOnePostBinding = FragmentOnePostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val postId = arguments?.idArg

        viewModel.data.observe(viewLifecycleOwner) { state ->
            val post = state.posts.find { it.id == postId } ?: return@observe

            // по идее, должно загружаться уже из кэша
            val url = "http://10.0.2.2:10999/avatars/${post.authorAvatar}"
            Glide.with(binding.icon)
                .load(url)
                .transform(RoundedCorners(32))
                .into(binding.icon)

            // слушатель для запуска просмотра видео
            val playVideoListener = OnClickListener{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(post.video)))
            }

            with(binding) {
                author.text = post.author
                published.text = post.published.toString();
                content.text = post.content
                tvPostId.setText("ID: " + post.id.toString())
                ibLikes.text = Utils.convert(post.likes)
                ibShares.text = Utils.convert(post.shares)
                ibLikes.isChecked = post.likedByMe
                tvViews.text = Utils.convert(post.views)
                if(post.video == "") {
                    ibVideo.visibility = View.GONE
                    ivThumbnail.visibility = View.GONE
                    ibPlayVideo.visibility = View.GONE
                }
                else {
                    ibVideo.visibility = View.VISIBLE
                    ivThumbnail.visibility = View.VISIBLE
                    ibPlayVideo.visibility = View.VISIBLE
                    val videoId = Uri.parse(post.video).getQueryParameter("v")
                    val thumbnailUri = Uri.parse("https://img.youtube.com/vi/${videoId}/0.jpg")
                    Glide.with(requireParentFragment()).load(thumbnailUri).into(binding.ivThumbnail)
                }

                ibLikes.setOnClickListener {
                    viewModel.likeById(post.id)
                }
                ibShares.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    startActivity(intent)
                    viewModel.share(post.id)
                }

                ibPlayVideo.setOnClickListener(playVideoListener)
                ivThumbnail.setOnClickListener(playVideoListener)
                ibVideo.setOnClickListener(playVideoListener)

                // слушатель на кнопку "три точки"
                buttonMenu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.remove(post.id)
                                    findNavController().navigateUp()
                                    true
                                }
                                R.id.add -> {
                                    val epost = Post (
                                        id = -1L,
                                        author = it.context.getString(R.string.title),
                                        content = "",
                                        published = 0L,
                                        likedByMe = false,
                                        likes = 0,
                                        shares = 0,
                                        views = 0,
                                        video = "",
                                        attachment = ""
                                    )
                                    viewModel.addAndEdit(epost)
                                    findNavController().navigateUp()
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.addAndEdit(post)
                                    findNavController().navigateUp()
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.idArg: Long? by IdArg
    }

}
