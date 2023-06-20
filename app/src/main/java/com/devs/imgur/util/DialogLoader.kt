package com.devs.imgur.util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.devs.imgur.R

class DialogLoader(context: Context) : Dialog(context) {
    init {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_loader)
            this.setCancelable(true)
            this.setCanceledOnTouchOutside(false)
            this.window?.setBackgroundDrawable(ColorDrawable(0))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
