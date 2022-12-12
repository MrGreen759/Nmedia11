package ru.netology.nmedia11

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia11.databinding.FragmentOnePostBinding

// Фрагмент просмотра карточки одного поста во весь экран

class OnePostFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOnePostBinding = FragmentOnePostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val postId = arguments?.idArg

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe

            // слушатель для запуска просмотра видео
            val playVideoListener = OnClickListener{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(post.video)))
            }

            with(binding) {
                author.text = post.author
                published.text = post.published
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
                    Glide.with(requireParentFragment()).load(thumbnailUri).into(binding.ivThumbnail) // TODO проверить "with"
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
                                        published = "",
                                        likes = 0,
                                        shares = 0,
                                        views = 0,
                                        video = ""
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
