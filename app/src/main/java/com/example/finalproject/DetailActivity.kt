package com.example.finalproject
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.finalproject.data.Item
import com.example.finalproject.data.PlaceDao
import com.example.finalproject.data.PlaceDatabase
import com.example.finalproject.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private val TAG = "Detail Activity"
    private val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private lateinit var db: PlaceDatabase
    private lateinit var placeDao: PlaceDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        val title = intent.getStringExtra("title")
        val address = intent.getStringExtra("address")
        val information = intent.getStringExtra("information")

        detailBinding.tvPlaceTitle.setText(title)
        detailBinding.tvPlaceInfo.setText(information)
        detailBinding.tvAddr.setText(address)

        db = PlaceDatabase.getDatabase(applicationContext)
        placeDao = db.placeDao()

        val fileManager: FileManager by lazy {
            FileManager(this)
        }

        detailBinding.btnSave.setOnClickListener {
            val place = Item(-1, title, address, information)


            // 코루틴을 사용하여 백그라운드 스레드에서 데이터베이스에 삽입 작업 수행
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    placeDao.insertPlace(place)
                }

                // UI 업데이트는 메인 스레드에서 수행
                Log.d(TAG, "Inserted into place_table: $title $address $information")
            }
        }
    }
}
