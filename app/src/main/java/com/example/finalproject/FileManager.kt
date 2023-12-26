package com.example.finalproject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import retrofit2.http.Url
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class FileManager(private val context: Context) {
    val TAG = "FileManager"

    fun writeText(fileName: String, data: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(data.toByteArray())
        }
    }

    fun readText(fileName: String) : String? {
        val result = StringBuffer()
        context.openFileInput(fileName).bufferedReader().useLines { lines ->
            for (line in lines) {
                result.append(line)

            }
        }
        return result.toString()
    }

    fun writeImage(fileName: String, imageUrl: String) {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object: CustomTarget<Bitmap> (350, 350) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.d(TAG, "Image load cleared!")
                }
            })
    }

    fun readInternetImage(url:String, view: ImageView) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(view)

    }

    fun deleteImage(fileName:String, filePath: String) {
        // 앱 내 지정된 저장소에서 파일 삭제 하기
        context.deleteFile(fileName)
        val deleteFile = File(filePath)
        deleteFile.delete()
    }

    fun readImage(fileName: String, view: ImageView) {
        // Glide를 활용해서 이미지 read
        Glide.with(context)
            .load(context.filesDir.path + "/$fileName")
            .into(view)
    }

    // Checks if a volume containing external storage is available
    // for read and write.
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // Checks if a volume containing external storage is available to at least read.
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    fun getImageFileName(path: String) : String {
        val fileName = path.slice(IntRange( path.lastIndexOf("/")+1, path.length-1))
        return fileName
    }

    // 파일 명을 현재 날짜로 저장
    fun getCurrentTime() : String {
        return SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    }
}