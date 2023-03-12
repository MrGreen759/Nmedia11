package ru.netology.nmedia11.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia11.*
import ru.netology.nmedia11.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia11.activity.OnePostFragment.Companion.idArg
import ru.netology.nmedia11.databinding.FragmentFeedBinding

// Отображение списка постов

class FeedFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFeedBinding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        var errOp = 0
        var errId = 0L

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val adapter = PostsAdapter (object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.addAndEdit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                startActivity(intent)
                viewModel.share(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.remove(post.id)
            }

            override fun onPost(id: Long) {
                // переход во фрагмент с одним постом
                findNavController().navigate(
                    R.id.action_feedFragment_to_onePostFragment,
                    Bundle().apply { idArg = id })
            }

            override fun onReload() {
                viewModel.loadPosts()
            }

        })

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == -1L) {
                return@observe
            }
            // переход во фрагмент редактирования текущего поста / создания нового поста
            findNavController().navigate(
                R.id.action_feedFragment_to_editPostFragment,
                Bundle().apply { textArg = post.content })
        }

        viewModel.errorPostId.observe(viewLifecycleOwner){ errId = it}
        viewModel.errorOperation.observe(viewLifecycleOwner) {
            if(it!=0) {
                binding.errorGroup.isVisible = true
                errOp = it
            }
        }

        binding.retryButton.setOnClickListener {
            when (errOp) {
                0 -> viewModel.loadPosts()
                1 -> viewModel.likeById(errId)
                2 -> viewModel.share(errId)
                3 -> viewModel.remove(errId)
            }
        }

        binding.cancelButton.setOnClickListener {
            binding.errorGroup.isVisible = false
            viewModel.errorOperation.value = 0
            errOp = 0
            errId = 0
        }

        return binding.root
    }

}

