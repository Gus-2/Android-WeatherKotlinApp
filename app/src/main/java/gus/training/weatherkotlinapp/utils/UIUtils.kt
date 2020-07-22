package gus.training.weatherkotlinapp.utils

import android.content.Context
import android.widget.Toast

fun Context.toasts(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}