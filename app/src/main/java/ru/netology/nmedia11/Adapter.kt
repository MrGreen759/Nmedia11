package ru.netology.nmedia11

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia11.databinding.PostCardBinding

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onShare(post: Post) {}
    fun onRemove(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : androidx.recyclerview.widget.ListAdapter<Post, PostViewHolder> (PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: OnInteractionListener
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
                onInteractionListener.onLike(post)
            }
            ibShares.setOnClickListener {
                val animZoom = AnimationUtils.loadAnimation(it.context, R.anim.scale_animation)
                binding.ibShares.startAnimation(animZoom)
                onInteractionListener.onShare(post)
            }

            // слушатель на кнопку "три точки"
            buttonMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
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
                                    views = 0
                                        )
                                onInteractionListener.onEdit(epost)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
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

class PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}
