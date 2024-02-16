package com.teleb.chefaaimagestask.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


fun File.deleteIfExists(){
    if (exists()) delete()
}
class ImageFileUtils @Inject constructor(private val context: Context) {

    private val timeStamp by lazy {
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    }

    fun saveBitmapToFile(bitmap: Bitmap): File {
        val imageFileName = "JPEG_$timeStamp.jpg"
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val imageFile = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        val outputStream = FileOutputStream(imageFile)

        // Compress the bitmap to JPEG format
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return imageFile
    }

    fun saveImageToFile(responseBody: ResponseBody): File {
        // Get the input stream and write it to a file
        val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)!!
        val imageFileName = "tinfyOutput_$timeStamp"

        val inputStream: InputStream = responseBody.byteStream()
        val outputFile = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        val outputStream = FileOutputStream(outputFile)
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.close()
        inputStream.close()
        return outputFile
    }

    fun createFileFromUri(context: Context, uri: Uri): File {
        val filePath = getRealPathFromUri(context, uri)!!
        return File(filePath)
    }

    private fun getRealPathFromUri(context: Context, uri: Uri): String? {
        var realPath: String? = null

        // Using the ContentResolver to get the real path from the URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        try {
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                realPath = cursor.getString(column_index)
                cursor.close()
            }
        } catch (e: Exception) {
            Log.e("FileUtil", "Error getting real path from URI", e)
        }
        return realPath
    }
}
