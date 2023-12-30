package com.example.finalproject.ui.home
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.finalproject.FileManager
import com.example.finalproject.data.Item
import com.example.finalproject.data.PlaceDao
import com.example.finalproject.data.PlaceDatabase
import com.example.finalproject.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        detailBinding.tvPlaceTitle.text = title
        detailBinding.tvPlaceInfo.text = address
        detailBinding.tvAddr.text = information

        db = PlaceDatabase.getDatabase(applicationContext)
        placeDao = db.placeDao()

        val fileManager: FileManager by lazy {
            FileManager(this)
        }

        detailBinding.btnSave.setOnClickListener {
            Log.d(TAG, "ADD Place: $title $address $information")
            addPlace(Item(0, title, information, address))
        }
        detailBinding.btnBack.setOnClickListener {
            finish()
        }

    }

    fun addPlace(place: Item) {
        CoroutineScope(Dispatchers.IO).launch {
            placeDao.insertPlace(place)
        }
    }
}
