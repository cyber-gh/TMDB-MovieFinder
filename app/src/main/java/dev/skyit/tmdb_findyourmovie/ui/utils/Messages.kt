package dev.skyit.tmdb_findyourmovie.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.snack(msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, msg, length)
        .show()
}

fun Context.errAlert(msg: String) {
    AlertDialog.Builder(this)
        .setTitle("Error")
        .setMessage(msg)
        .setPositiveButton("OK") { _: DialogInterface, i: Int -> }
        .show()
}

fun Fragment.snack(msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    requireView().snack(msg, length)
}

fun Fragment.errAlert(msg: String) {
    requireContext().errAlert(msg)
}

fun Fragment.toastl(msg: String) {
    requireContext().toastl(msg)
}

fun Context.toastl(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}