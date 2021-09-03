package com.mad.mad_bookworms

import android.R.attr
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.Blob
import java.io.ByteArrayOutputStream
import android.R.attr.label

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import android.R.attr.label
import android.app.PendingIntent.getActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService
import java.security.AccessController.getContext


// Usage: Show an error dialog from fragment
fun Fragment.errorDialog(text: String) {
    AlertDialog.Builder(context)
        .setIcon(R.drawable.ic_error)
        .setTitle("Error")
        .setMessage(text)
        .setPositiveButton("Dismiss", null)
        .show()
}

fun showMultiuseDialog(activity: Activity?, type: Int, title:String, content :String) {
    val dialog = Dialog(activity!!)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_multiuse)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val imgDialog = dialog.findViewById<ImageView>(R.id.imgDialog)
    val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
    val dialogContent = dialog.findViewById<TextView>(R.id.dialogContent)
    val dialogBtn_remove = dialog.findViewById<Button>(R.id.btnOK)

    if(type == 1)
    {
        imgDialog.setImageResource(R.drawable.reward_not_eligible);

    }else if(type == 2){

        imgDialog.setImageResource(R.drawable.reward_claim);

    }else if(type == 3){

        imgDialog.setImageResource(R.drawable.reward_not_enough);
    }

    dialogTitle.text = title
    dialogContent.text = content

    dialogBtn_remove.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun showUseDialog(activity: Activity?, code: String) {
    val dialog = Dialog(activity!!)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_use_voucher)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val dialogCode = dialog.findViewById<TextView>(R.id.tvCode)
    val dialogBtn_copy = dialog.findViewById<Button>(R.id.btnCopy)

    dialogCode.text = code

    dialogBtn_copy.setOnClickListener {

        dialogBtn_copy.text = "copied"

        // Copy Text to the Clipboard
        copyToClipboard(activity,code)

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 1500)

    }

    dialog.show()
}

fun showEmailDialog(activity: Activity?, type: Int, title:String) {

    val animFadeIn: Animation =
        AnimationUtils.loadAnimation(activity, android.R.anim.fade_in)
    val animFadeOut: Animation =
        AnimationUtils.loadAnimation(activity, android.R.anim.fade_out)

    val dialog = Dialog(activity!!)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_send_email)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val imgDialog = dialog.findViewById<ImageView>(R.id.imgDialog)
    val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)

    if(type == 1)
    {
        imgDialog.startAnimation(animFadeIn)
        imgDialog.setImageResource(R.drawable.done);

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 3500)

    }else if(type == 2){

        imgDialog.startAnimation(animFadeIn)
        imgDialog.setImageResource(R.drawable.load);

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 2000)

    }

    dialogTitle.startAnimation(animFadeIn)
    dialogTitle.text = title

    dialog.show()
}

fun copyToClipboard(context: Context?, text :String)
{
    val clipboard = ContextCompat.getSystemService(context!!, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("label",text)
    clipboard!!.setPrimaryClip(clip)
}

// Usage: Crop and resize bitmap (upscale)
fun Bitmap.crop(width: Int, height: Int): Bitmap {
    // Source width, height and ratio
    val sw = this.width
    val sh = this.height
    val sratio = 1.0 * sw / sh

    // Target offset (x, y), width, height and ratio
    val x: Int
    val y: Int
    val w: Int
    val h: Int
    val ratio = 1.0 * width / height

    if (ratio >= sratio) {
        // Retain width, calculate height
        w = sw
        h = (sw / ratio).toInt()
        x = 0
        y = (sh - h) / 2
    }
    else {
        // Retain height, calculate width
        w = (sh * ratio).toInt()
        h = sh
        x = (sw - w) / 2
        y = 0
    }

    return Bitmap
        .createBitmap(this, x, y, w, h) // Crop
        .scale(width, height) // Resize
}

// Usage: Convert from Bitmap to Blob
@Suppress("DEPRECATION")
fun Bitmap.toBlob(): Blob {
    ByteArrayOutputStream().use {
        this.compress(Bitmap.CompressFormat.WEBP, 80, it)
        return Blob.fromBytes(it.toByteArray())
    }
}

// Usage: Convert from Blob to Bitmap
fun Blob.toBitmap(): Bitmap? {
    val bytes = this.toBytes()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

// Usage: Crop to Blob
fun ImageView.cropToBlob(width: Int, height: Int): Blob {
    if (this.drawable == null)
        return Blob.fromBytes(ByteArray(0))
    else
        return this.drawable.toBitmap().crop(width, height).toBlob()
}

