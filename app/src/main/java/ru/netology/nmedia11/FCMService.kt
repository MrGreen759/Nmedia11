package ru.netology.nmedia11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlin.random.Random


//f-WmfEEpSJK7NCnsB6Hj4R:APA91bHrGLtJtabn0PzM-_lVFbO-kXgHOXO7NhNCeYa4TtokL9KrdIOUKpD-JdZZR103_Ahjz7h6iJXVfzS51Y8yvnZS3g8msAbTv3EvmMqJ6qX6AjLkyPPmMeIRrvt6cftV2ss5XODa

class FCMService : FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.remote_channel_name)
            val descriptionText = getString(R.string.remote_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        try {
            message.data[action]?.let {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(gson.fromJson(message.data[content], Like::class.java))
                    Action.NEWPOST -> handleNew(gson.fromJson(message.data[content], NewPost::class.java))
                }
            }
        } catch (e: RuntimeException) {
            println("catch exception !!!")
            return
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    private fun handleLike(content: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleNew(content: NewPost) {
        val oneString = content.postContent.substring(0,30) + "..."
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_newpost,
                    content.userName
                )
            )
            .setContentText(oneString)
//            .setLargeIcon(emailObject.getSenderAvatar())
            .setStyle(NotificationCompat.BigTextStyle().bigText(content.postContent))
            .addAction(R.drawable.ic_read, getString(R.string.mark_as_read), null)
            .addAction(R.drawable.ic_open_messager, getString(R.string.open_messager), null)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

}

enum class Action {
    LIKE,
    NEWPOST
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
    )

data class NewPost (
    val userName: String,
    val postContent: String
    )
