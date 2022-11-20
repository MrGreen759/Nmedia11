package ru.netology.nmedia11

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edirGroup.visibility = View.GONE

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter (
            { viewModel.likeById(it.id) },
            { viewModel.share(it.id) },
            { viewModel.remove(it.id) },
            { viewModel.addAndEdit(it) }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                binding.edirGroup.visibility = View.VISIBLE
                binding.content.requestFocus()
                setText(post.content)
                val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(0,0)
            }
        }

        // на кнопку "Save"
        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.windowToken, 0)
                binding.edirGroup.visibility = View.GONE
            }
        }

        // на кнопку "Cancel"
        binding.cancel.setOnClickListener {
            binding.content.setText("")
            binding.content.clearFocus()
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.content.windowToken, 0)
            binding.edirGroup.visibility = View.GONE
        }

    }
}
