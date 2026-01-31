package com.wildticker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.getSystemService
import com.wildticker.data.NewsItem
import com.wildticker.repository.NewsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class OverlayService : Service() {
    override fun onCreate() {
        super.onCreate()
        startForegroundServiceWithNotification()
        createOverlayViews()
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "wildticker_overlay"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            nm?.createNotificationChannel(
                NotificationChannel(channelId, "Overlay", NotificationManager.IMPORTANCE_LOW)
            )
        }
        val notif = Notification.Builder(this, channelId)
            .setContentTitle("Wild Ticker running")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        startForeground(1, notif)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @Inject
    lateinit var repo: NewsRepository

    private var wm: WindowManager? = null
    private var root: LinearLayout? = null
    private var collectJob: Job? = null

    private fun createOverlayViews() {
        wm = getSystemService()
        if (wm == null) return

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            -3
        )
        params.gravity = Gravity.BOTTOM

        root = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(0x00000000)
        }

        val icon = ImageView(this).apply {
            setImageResource(android.R.drawable.ic_dialog_info)
            alpha = 0.6f
            setOnClickListener {
                val i = Intent(this@OverlayService, SettingsActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            }
        }

        val marquee = TextView(this).apply {
            text = "Loading headlines..."
            isSingleLine = true
            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1
            isSelected = true
            alpha = 0.9f
        }

        root?.addView(icon)
        root?.addView(marquee)

        // Drag support for icon
        var lastX = 0f
        var lastY = 0f
        icon.setOnTouchListener { v, ev ->
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = ev.rawX
                    lastY = ev.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = (ev.rawX - lastX).toInt()
                    val dy = (ev.rawY - lastY).toInt()
                    lastX = ev.rawX
                    lastY = ev.rawY
                    try {
                        val lp = v.layoutParams as WindowManager.LayoutParams
                        lp.x += dx
                        lp.y += dy
                        wm?.updateViewLayout(v, lp)
                    } catch (_: Exception) {}
                    true
                }
                else -> false
            }
        }

        try {
            wm?.addView(root, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Observe cached headlines and update marquee
        collectJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                repo.cached().collect { items ->
                    if (items.isNotEmpty()) {
                        val t = items.joinToString(" \u2022 ") { it.title }
                        CoroutineScope(Dispatchers.Main).launch { marquee.text = t }
                    }
                }
            } catch (e: Exception) { /* ignore collect errors */ }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        collectJob?.cancel()
        try { root?.let { wm?.removeView(it) } } catch (_: Exception) {}
    }
}
