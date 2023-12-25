package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.finalproject.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    val TAG = "FILEMANAGER"
    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    // file manager 객체 생성
    val fileManager: FileManager by lazy {
        FileManager(applicationContext)
    }
    /* 파일 저장시간 저장 -> 파일 save, read, delete에 사용 */
    var savedTime : String? = null
    var recentFile : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        // mainActivity -> 이미지 url
        val url = intent.getStringExtra("url")
        // 이미지 url 확인
        Log.d(TAG, url.toString())

        detailBinding.btnSave.setOnClickListener {
            savedTime = fileManager.getCurrentTime()
            recentFile = savedTime
            fileManager.writeImage("${savedTime}.jpg", url.toString())
        }

        detailBinding.btnRead.setOnClickListener {
            Log.d(TAG, "load image"+ savedTime);
            if (url != null) {
                Log.d("이미지 로드 중인데", "null아님")
                fileManager.readInternetImage(url, detailBinding.imgBookCover)
            }
        }

        detailBinding.btnInit.setOnClickListener {
            detailBinding.imgBookCover.setImageResource(R.mipmap.ic_launcher)
        }

        detailBinding.btnRemove.setOnClickListener {
            // 파일 명으로 삭제 하기
            fileManager.deleteImage("${savedTime}.jpg", url.toString())
        }
    }
}