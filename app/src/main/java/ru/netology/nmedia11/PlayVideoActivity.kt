package ru.netology.nmedia11

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ru.netology.nmedia11.databinding.ActivityPlayVideoBinding

class PlayVideoActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlayVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ref = intent.getStringExtra("refToVideo")

        // получаем миниатюру
        val videoId = Uri.parse(ref).getQueryParameter("v")
        val thumbnailUri = Uri.parse("https://img.youtube.com/vi/${videoId}/0.jpg")
        Glide.with(this).load(thumbnailUri).into(binding.ivThumbnail)

        // слушатель
        val onClicker = OnClickListener {
            val playVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(ref))
            startActivity(playVideoIntent)
            finish()
        }

        binding.ivThumbnail.setOnClickListener(onClicker)
        binding.ibPlayVideo.setOnClickListener(onClicker)

    }
}