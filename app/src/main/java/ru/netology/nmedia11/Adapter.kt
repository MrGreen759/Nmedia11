package ru.netology.nmedia11

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia11.databinding.PostCardBinding
import java.security.PrivateKey

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit
typealias OnRemoveListener = (post: Post) -> Unit
typealias OnEditListener = (post: Post) -> Unit

class PostsAdapter(
    private val onLikeListener: OnLikeListener /* = (post: ru.netology.nmedia11.Post) -> kotlin.Unit */,
    private val onShareListener: OnShareListener /* = (post: ru.netology.nmedia11.Post) -> kotlin.Unit */,
    private val onRemoveListener: OnRemoveListener,
    private val onEditListener: OnEditListener
) : androidx.recyclerview.widget.ListAdapter<Post, PostViewHolder> (PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener, onRemoveListener, onEditListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder (
    private val binding: PostCardBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener,
    private val onRemoveListener: OnRemoveListener,
    private val onEditListener: OnEditListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            tvLikes.text = convert(post.likes)
            tvShares.text = convert(post.shares)
            tvPostId.text = post.id.toString()
            tvViews.text = convert(post.views)
            if (post.likedByMe) {
                ibLikes.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                ibLikes.setImageResource(R.drawable.ic_outline_favorite_border_24)
            }
            ibLikes.setOnClickListener {
                onLikeListener(post)
            }
            ibShares.setOnClickListener {
                val animZoom = AnimationUtils.loadAnimation(it.context, R.anim.scale_animation)
                binding.ibShares.startAnimation(animZoom)
                onShareListener(post)
            }
            buttonMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onRemoveListener(post)
                                true
                            }
                            R.id.add -> {
                                var epost = Post (
                                    id = 0L,
                                    author = "",
                                    content = "",
                                    published = "",
                                    likes = 0,
                                    shares = 0,
                                    views = 0
                                        )
                                onEditListener(epost)
                                true
                            }
                            R.id.edit -> {
                                onEditListener(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

    // на входе - число, на выходе строка типа "999" или "1К" или "2,2М"
    private fun convert(num: Int): String {
        val form: String
        val n: Int
        when(num) {
            in 0 .. 999 -> return num.toString()
            in 1000 .. 9999 -> {
                n = num%1000
                form = (if((n < 100)||(n>900)) "%.0f" else "%.1f")
                return String.format(form, num.toDouble()/1000) + "K"
            }
            in 10000 .. 999999 -> return String.format("%.0f", num.toDouble()/1000) + "K"
            else -> {
                n = num%1000000
                form = (if((n < 100000)||(n>900000)) "%.0f" else "%.1f")
                return String.format(form, num.toDouble()/1000000) + "M"
            }
        }
    }

}

class PostDiffCallback(): DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}
