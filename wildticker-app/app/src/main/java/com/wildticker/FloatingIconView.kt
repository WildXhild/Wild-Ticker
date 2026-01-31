package com.wildticker

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView

class FloatingIconView(private val ctx: Context) {
    fun create(): ImageView {
        val iv = ImageView(ctx)
        iv.setImageResource(android.R.drawable.ic_dialog_info)
        iv.alpha = 0.6f
        iv.contentDescription = "Wild ticker icon"
        return iv
    }
}
