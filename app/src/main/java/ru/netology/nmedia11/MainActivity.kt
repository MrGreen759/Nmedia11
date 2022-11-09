package ru.netology.nmedia11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import ru.netology.nmedia11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                tvLikes.text = convert(post.likes)
                tvShares.text = convert(post.shares)
                tvViews.text = convert(post.views)
                if (post.likedByMe) {
                    ibLikes.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    ibLikes.setImageResource(R.drawable.ic_outline_favorite_border_24)
                }
            }
        }

        // слушатель на кнопку "like"
        binding.ibLikes.setOnClickListener {
            viewModel.like()
        }

        // слушатель на кнопку "share"
        binding.ibShares.setOnClickListener {
            val animZoom =
                AnimationUtils.loadAnimation(applicationContext, R.anim.scale_animation)
            binding.ibShares.startAnimation(animZoom)
            viewModel.share()
        }

    }

    // на входе - число, на выходе строка типа "999" или "1К" или "2,2М"
    fun convert(num: Int): String {
        val form: String
        val n: Int
        when(num) {
            in 1 .. 999 -> return num.toString()
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
