package com.example.demo9

import android.content.Context
import android.text.Html
import android.widget.Toast


class CustomToast {
    var context: Context
    var message = ""

    constructor(context: Context, message: String) {
        this.context = context
        this.message = message
    }

    fun showToast() {
        var toast = Toast.makeText(
            context,
            Html.fromHtml("<font color='#3498DB'><b>" + message + "</b></font>"),
            Toast.LENGTH_SHORT
        )
        toast.show()
    }
}