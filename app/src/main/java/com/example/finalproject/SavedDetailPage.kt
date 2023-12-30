package com.example.finalproject
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.data.Item
import com.example.finalproject.data.PlaceDao
import com.example.finalproject.data.PlaceDatabase
import com.example.finalproject.databinding.ActivitySavedPlaceBinding
import com.example.finalproject.ui.home.PlaceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedDetailPage : AppCompatActivity() {
    private val TAG = "SavedDetailPage Activity"
    private val savedBinding by lazy {
        ActivitySavedPlaceBinding.inflate(layoutInflater)
    }

    private lateinit var db: PlaceDatabase
    private lateinit var placeDao: PlaceDao
    private val adapter: PlaceAdapter = PlaceAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(savedBinding.root)

        savedBinding.rvPlaces.adapter = adapter
        savedBinding.rvPlaces.layoutManager = LinearLayoutManager(this)

        db = PlaceDatabase.getDatabase(applicationContext)
        placeDao = db.placeDao()

        adapter.places = getPlaces()
        adapter.notifyDataSetChanged()

    }
    private fun addPlace(place: Item) {
        CoroutineScope(Dispatchers.IO).launch {
            placeDao.insertPlace(place)
        }
        Log.d(TAG, "Inserted into place_table: $place.title $place.address $place.information")
    }
    private fun getPlaces() : List<Item>? {
        Log.d(TAG, "GET all saved Places")
        lateinit var places : List<Item>
        CoroutineScope(Dispatchers.IO).launch {
            places = placeDao.getAllPlaces()
        }
        return places
    }
}
