package ru.netology.nmedia11

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import ru.netology.nmedia11.databinding.FragmentOnePostBinding

class OnePostFragment : Fragment() {

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
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                tvPostId.text = "ID: " + post.id.toString()
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
            }


        }
        return binding.root
    }

    companion object {
        var Bundle.idArg: Long? by IdArg
    }

}
