package com.ted.roomwordssample.utils

import android.app.Dialog
import android.content.Context
import com.ted.roomwordssample.R

object CustomAlertDialog {

    lateinit var loadingdialog: Dialog
    fun showLoadingDialog(context: Context) {
        loadingdialog = Dialog(context, R.style.CustomAlertDialog)
        loadingdialog.setContentView(R.layout.dialog_layout)
        loadingdialog.setCancelable(true)
        val width = (context.resources.displayMetrics.widthPixels * 0.80).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.30).toInt()
        loadingdialog.window!!.setLayout(width, height)
        loadingdialog.create()
        loadingdialog.show()
    }

    fun dismissLoadingDialog(){
        if (loadingdialog != null){
            loadingdialog.dismiss()
        }
    }
}