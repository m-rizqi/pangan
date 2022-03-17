package com.satulima.pangan.utility

import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import java.text.SimpleDateFormat
import java.util.*

fun setTransparentStatusBar(window: Window){
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = Color.TRANSPARENT
    }
}

fun Date.toString(): String{
    return SimpleDateFormat("dd/MM/yyyy").format(this)
}

fun String.toDate(): Date{
    return SimpleDateFormat("dd/MM/yyyy").parse(this)
}