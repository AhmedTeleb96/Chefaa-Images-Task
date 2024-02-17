package com.teleb.chefaaimagestask.presentation.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    fun shareImage(context: Context, bitmap: Bitmap) {
        val imagePath = saveImageToInternalStorage(bitmap)
        val imageFile = File(imagePath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)

        // Share the image
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    fun saveImage(bitmap: Bitmap):Boolean {
       return saveImageToInternalStorage(bitmap).isNotEmpty()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, "${System.currentTimeMillis()}_image.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        return file.absolutePath
    }
}