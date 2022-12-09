package ru.netology.nmedia11

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia11.EditPostFragment.Companion.textArg
import ru.netology.nmedia11.OnePostFragment.Companion.idArg
//import ru.netology.nmedia11.databinding.ActivityMainBinding
import ru.netology.nmedia11.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val viewModel: PostViewModel by viewModels()
//        val adapter = PostsAdapter (object : OnInteractionListener {
//            override fun onEdit(post: Post) {
//                viewModel.addAndEdit(post)
//            }
//
//            override fun onLike(post: Post) {
//                viewModel.likeById(post.id)
//            }
//
//            override fun onShare(post: Post) {
//                val intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, post.content)
//                    type = "text/plain"
//                }
//                startActivity(intent)
//                viewModel.share(post.id)
//            }
//
//            override fun onRemove(post: Post) {
//                viewModel.remove(post.id)
//            }
//
//            override fun onPlay(post: Post) {
//                super.onPlay(post)
//                val toVideoIntent = Intent(this@FeedFragment, PlayVideoActivity::class.java)
//                val mBundle = Bundle()
//                mBundle.putString("refToVideo", post.video)
//                toVideoIntent.putExtras(mBundle)
//                startActivity(toVideoIntent, mBundle)
//            }
//        })
//
//        binding.list.adapter = adapter
//
//        val editPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
//            result ?: return@registerForActivityResult
//            viewModel.changeContent(result)
//            viewModel.save()
//        }
//
//        viewModel.data.observe(this) { posts ->
//            adapter.submitList(posts)
//        }
//
//        viewModel.edited.observe(this) { post ->
//            if (post.id == 0L) {
//                return@observe
//            }
//            editPostLauncher.launch(post.content)
//        }
//
//    }

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
        //setContentView(binding.root)
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

//            override fun onPlay(post: Post) {
//                super.onPlay(post)
//                val toVideoIntent = Intent(this@FeedFragment, PlayVideoActivity::class.java)
//                val mBundle = Bundle()
//                mBundle.putString("refToVideo", post.video)
//                toVideoIntent.putExtras(mBundle)
//                startActivity(toVideoIntent, mBundle)
//            }

            override fun onPost(id: Long) {
                findNavController().navigate(R.id.action_feedFragment_to_onePostFragment,
                    Bundle().apply { idArg = id })
            }

        })

        binding.list.adapter = adapter

//        val editPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
//            result ?: return@registerForActivityResult
//            viewModel.changeContent(result)
//            viewModel.save()
//        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }
            //editPostLauncher.launch(post.content)
            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment,
                Bundle().apply { textArg = post.content })
        }
        //return super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

}

