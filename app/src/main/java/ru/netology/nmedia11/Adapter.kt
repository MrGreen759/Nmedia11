package ru.netology.nmedia11

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia11.EditPostFragment.Companion.textArg
import ru.netology.nmedia11.OnePostFragment.Companion.idArg
import ru.netology.nmedia11.databinding.PostCardBinding

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onShare(post: Post) {}
    fun onRemove(post: Post) {}
    fun onPlay(post: Post) {}
    fun onPost(id: Long) {}
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
            tvPostId.text = "ID: " + post.id.toString()
            tvViews.text = Utils.convert(post.views)

            ibLikes.text = Utils.convert(post.likes)
            ibShares.text = Utils.convert(post.shares)

            ibLikes.isChecked = post.likedByMe

            ibLikes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            ibShares.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            if(post.video == "") ibVideo.visibility = View.GONE
            else ibVideo.visibility = View.VISIBLE

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
                                    views = 0,
                                    video = ""
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
            ibVideo.setOnClickListener {
                onInteractionListener.onPlay(post)
            }
            author.setOnClickListener {
                onInteractionListener.onPost(post.id)
            }
            published.setOnClickListener {
                onInteractionListener.onPost(post.id)
            }
            content.setOnClickListener {
                onInteractionListener.onPost(post.id)
            }
            ivViews.setOnClickListener {
                onInteractionListener.onPost(post.id)
            }
        }
    }

    // конвертер: на входе - число, на выходе строка типа "999" или "1К" или "2,2М"
//     fun convert(num: Int): String {
//        val form: String
//        val n: Int
//        when(num) {
//            in 0 .. 999 -> return num.toString()
//            in 1000 .. 9999 -> {
//                n = num%1000
//                form = (if((n < 100)||(n>900)) "%.0f" else "%.1f")
//                return String.format(form, num.toDouble()/1000) + "K"
//            }
//            in 10000 .. 999999 -> return String.format("%.0f", num.toDouble()/1000) + "K"
//            else -> {
//                n = num%1000000
//                form = (if((n < 100000)||(n>900000)) "%.0f" else "%.1f")
//                return String.format(form, num.toDouble()/1000000) + "M"
//            }
//        }
//    }

}

class PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}
