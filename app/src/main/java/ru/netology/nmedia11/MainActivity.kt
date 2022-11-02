package ru.netology.nmedia11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import ru.netology.nmedia11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 Мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 184,
            views = 2898
        )

        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            tvLikes.text = convert(post.likes)
            tvShares.text = convert(post.shares)
            tvViews.text = convert(post.views)

            // слушатель на кнопку "like"
            ibLikes.setOnClickListener {
                post.likedByMe = !post.likedByMe
                if(post.likedByMe) {
                    ibLikes.setImageResource(R.drawable.ic_baseline_favorite_24)
                    tvLikes.text = convert(++post.likes)
                } else {
                    ibLikes.setImageResource(R.drawable.ic_outline_favorite_border_24)
                    tvLikes.text = convert(--post.likes)
                }
            }

            // слушатель на кнопку "share"
            ibShares.setOnClickListener{
                val animZoom = AnimationUtils.loadAnimation(applicationContext, R.anim.scale_animation)
                ibShares.startAnimation(animZoom)
                tvShares.text = convert(++post.shares)
            }
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
