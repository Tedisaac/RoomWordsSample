package com.ted.roomwordssample.utils

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.ted.roomwordssample.R

object CustomAlertDialog {

    lateinit var loadingdialog: Dialog
    fun showLoadingDialog(context: Context) {
        loadingdialog = Dialog(context, R.style.CustomAlertDialog)
        loadingdialog.setContentView(R.layout.dialog_layout)
        loadingdialog.setCancelable(true)
        val width = (context.resources.displayMetrics.widthPixels * 0.80).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.18).toInt()
        loadingdialog.window!!.setLayout(width, height)
        loadingdialog.create()
        loadingdialog.show()
    }

    fun dismissLoadingDialog(){
        if (loadingdialog != null){
            loadingdialog.dismiss()
        }
    }

    fun showConfirmDialog(context: Context, titleString: String, messageString: String, onYesClicked: () -> Unit, onNoClicked: () -> Unit) {
        loadingdialog = Dialog(context, R.style.CustomAlertDialog)
        loadingdialog.setContentView(R.layout.confirm_dialog_layout)
        loadingdialog.setCancelable(true)

        val title: TextView = loadingdialog.findViewById(R.id.txtConfirmDialogTitle)
        val message: TextView = loadingdialog.findViewById(R.id.txtConfirmDialogMessage)
        val yes: TextView = loadingdialog.findViewById(R.id.txtConfirmDialogYes)
        val no: TextView = loadingdialog.findViewById(R.id.txtConfirmDialogNo)

        title.text = titleString
        message.text = messageString
        yes.setOnClickListener { onYesClicked() }
        no.setOnClickListener { onNoClicked() }

        val width = (context.resources.displayMetrics.widthPixels * 0.80).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.18).toInt()
        loadingdialog.window!!.setLayout(width, height)

        loadingdialog.create()
        loadingdialog.show()
    }
}